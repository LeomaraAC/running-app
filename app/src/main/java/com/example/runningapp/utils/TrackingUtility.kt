package com.example.runningapp.utils

import java.util.concurrent.TimeUnit

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
}