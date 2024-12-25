package com.example.runningapp.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.runningapp.R
import com.example.runningapp.utils.Constants.ACTION_PAUSE_SERVICE
import com.example.runningapp.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningapp.utils.Constants.ACTION_STOP_SERVICE
import com.example.runningapp.utils.Constants.FASTEST_LOCATION_INTERVAL
import com.example.runningapp.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.example.runningapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.runningapp.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.runningapp.utils.Constants.NOTIFICATION_ID
import com.example.runningapp.utils.Constants.ONE_SECOND_IN_MILLIS
import com.example.runningapp.utils.Constants.TIME_UPDATE_INTERVAL
import com.example.runningapp.utils.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService : LifecycleService() {
    var isFirstRun = true

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var curNotificationBuilder: NotificationCompat.Builder

    private val timeRunInSeconds = MutableStateFlow<Long>(0L)
    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    companion object {
        val pathPoints = MutableStateFlow<Polylines> (mutableListOf())
        val isTracking = MutableStateFlow(false)
        val timeRunInMillis = MutableStateFlow<Long>(0L)
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        curNotificationBuilder = baseNotificationBuilder
        postInitialValue()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        lifecycleScope.launch {
            isTracking.collect{
                updateLocationTracking(it)
                updateNotificationTrackingState(it)
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        startTimer()
                    }
                    Timber.d("Started or resumed service")
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("paused service")
                    pauseService()
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("sttoped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer() {
        addEmptyPolylines()
        isTracking.value = true
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInMillis.value = timeRun + lapTime
                if (timeRunInMillis.value >= lastSecondTimestamp + ONE_SECOND_IN_MILLIS) {
                    timeRunInSeconds.value++
                    lastSecondTimestamp += ONE_SECOND_IN_MILLIS
                }
                delay(TIME_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pausar" else "Continuar"
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        curNotificationBuilder = baseNotificationBuilder.addAction(
            R.drawable.pause_icon,
            notificationActionText,
            pendingIntent
        )
        notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())
    }

    private fun pauseService() {
        isTracking.value = false
        isTimerEnabled = false
    }

    private fun updateLocationTracking(isTracking: Boolean) {
        if (!isTracking) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            return
        }

        val hasPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            return
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        )
            .setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL).build()

        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun postInitialValue() {
        isTracking.value  = false
        pathPoints.value = mutableListOf()
        timeRunInMillis.value = 0L
        timeRunInSeconds.value = 0L
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(it.latitude, it.longitude)
            val updatedPathPoints = clonePathPoints()
            if (updatedPathPoints.isNotEmpty()) {
                updatedPathPoints.last().add(pos)
            } else {
                updatedPathPoints.add(mutableListOf(pos))
            }

            pathPoints.value = updatedPathPoints
        }
    }

    private fun addEmptyPolylines() {
        val updatedPathPoints = clonePathPoints()
        updatedPathPoints.apply { add(mutableListOf()) }
        pathPoints.value = updatedPathPoints
    }

    private fun startForegroundService() {
        startTimer()
        isTracking.value = true

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            baseNotificationBuilder.build(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                0
            }
        )

        lifecycleScope.launch {
            timeRunInSeconds.collect {
                val notification = curNotificationBuilder
                    .setContentText(TrackingUtility.getFormattedStopWatchTime(it * 1000L))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        }

    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun clonePathPoints(): Polylines {
       return pathPoints.value.map { it.toMutableList() }.toMutableList()
    }

}