package com.example.runningapp.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.db.entity.Run
import com.example.runningapp.repositories.RunRepository
import com.example.runningapp.services.Polylines
import com.example.runningapp.services.TrackingService
import com.example.runningapp.utils.TrackingUtility
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(private val runRepository: RunRepository) : ViewModel() {
    private val _pathPoints = MutableStateFlow<Polylines>(mutableListOf())
    private val _isTracking = MutableStateFlow(false)
    private val _curTimeFormatted = MutableStateFlow("00:00:00:00")
    private var timesInMillis = 0L

    val pathPoints: StateFlow<Polylines> get() = _pathPoints
    val isTracking: StateFlow<Boolean> get() = _isTracking
    val curTimeFormatted: StateFlow<String> get() = _curTimeFormatted

    init {
        viewModelScope.launch {
            TrackingService.pathPoints.collect { newPathPoints ->
                _pathPoints.value = newPathPoints
            }
        }

        viewModelScope.launch {
            TrackingService.isTracking.collect { _isTracking.value = it }
        }

        viewModelScope.launch {
            TrackingService.timeRunInMillis.collect {
                timesInMillis = it
                _curTimeFormatted.value = TrackingUtility.getFormattedStopWatchTime(it, true)
            }
        }
    }

    fun getCurrentPosition(pathPoints: Polylines): LatLng {
        if (pathPoints.isEmpty()) {
            return LatLng(0.0, 0.0)
        }
        if (pathPoints.last().isNotEmpty()) {
            return pathPoints.last().last()
        }

        if (pathPoints.size > 1) {
            return pathPoints[pathPoints.size - 2].last()
        }

        return LatLng(0.0, 0.0)
    }

    fun saveRun(snapshot: Bitmap?) {
        val distanceInMeters = pathPoints.value.sumOf { polyline ->
            TrackingUtility.calculatePolylineLength(polyline).toInt()
        }
        val avgSpeed = TrackingUtility.calculateAvgSpeed(distanceInMeters, timesInMillis)
        val dateTimestamp = Calendar.getInstance().timeInMillis
        val caloriesBurned = TrackingUtility.calculateCaloriesBurned(
            distanceInMeters,
            80f
        ) //todo -> Ajustar o valor de weight

        val run = Run(
            img = snapshot,
            timestamp = dateTimestamp,
            avgSpeedInKMH = avgSpeed,
            distanceInMeters = distanceInMeters,
            timeInMillis = timesInMillis,
            caloriesBurned = caloriesBurned
        )

        viewModelScope.launch {
            runRepository.insertRun(run)
        }
    }
}