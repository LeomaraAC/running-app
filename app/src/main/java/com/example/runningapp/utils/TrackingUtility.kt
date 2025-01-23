package com.example.runningapp.utils

import android.location.Location
import com.example.runningapp.services.Polyline
import com.example.runningapp.utils.Constants.METERS_TO_KILOMETERS
import com.example.runningapp.utils.Constants.MILLIS_TO_HOURS
import java.util.concurrent.TimeUnit
import kotlin.math.round

object TrackingUtility {
    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        val formattedHours = if (hours < 10) "0$hours" else hours
        val formattedMinutes = if (minutes < 10) "0$minutes" else minutes
        val formattedSeconds = if (seconds < 10) "0$seconds" else seconds

        if (!includeMillis) {
            return "$formattedHours:$formattedMinutes:$formattedSeconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        val formattedMilliseconds = if (milliseconds < 10) "0$milliseconds" else milliseconds
        return "$formattedHours:$formattedMinutes:$formattedSeconds:$formattedMilliseconds"
    }

    fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f
        for (i in 0..polyline.size - 2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun calculateAvgSpeed(distanceInMeters: Int, timeInMillis: Long): Float {
        return round((distanceInMeters / METERS_TO_KILOMETERS) / (timeInMillis / MILLIS_TO_HOURS) * 10) / 10f
    }

    fun calculateCaloriesBurned(distanceInMeters: Int, weight: Float): Int {
        return ((distanceInMeters / METERS_TO_KILOMETERS) * weight).toInt()
    }
}