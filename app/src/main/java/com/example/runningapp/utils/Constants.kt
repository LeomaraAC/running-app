package com.example.runningapp.utils

import androidx.compose.ui.graphics.Color

object Constants {
    const val RUNNING_DATABASE_NAME = "running_db"

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L
    const val ONE_SECOND_IN_MILLIS = 1000L
    const val TIME_UPDATE_INTERVAL = 50L

    val POLYLINE_COLOR = Color.Red
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 17f

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val METERS_TO_KILOMETERS = 1000f
    const val MILLIS_TO_HOURS = 3600000f
}