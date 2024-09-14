package com.example.runningapp.ui.navigation

import androidx.annotation.DrawableRes

interface NavigationDestination {
    val route: String
    val title: String?
    @get:DrawableRes
    val icon: Int?
}
