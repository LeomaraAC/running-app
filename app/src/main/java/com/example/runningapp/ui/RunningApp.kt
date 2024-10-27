package com.example.runningapp.ui

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.runningapp.ui.navigation.RunNavHost
import com.example.runningapp.ui.screens.HomeDestination
import com.example.runningapp.ui.screens.SettingsDestination
import com.example.runningapp.ui.screens.StatisticsDestination
import com.example.runningapp.ui.screens.TrackingDestination
import com.example.runningapp.utils.Constants.ACTION_SHOW_TRACKING_FRAGMENT

@Composable
fun RunningApp(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    intent: Intent
) {
    val action = intent.action
    LaunchedEffect(action) {
        if (action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostController.navigate(TrackingDestination.route)
        }
    }

    Scaffold(
        bottomBar = { BottomBar(navController = navHostController) }
    ) { innerPadding ->
        RunNavHost(
            navController = navHostController,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        HomeDestination,
        StatisticsDestination,
        SettingsDestination
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = screens.any{ it.route == currentDestination?.route}
    if (!showBottomBar)
        return

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = screen.icon!!), contentDescription = screen.title) },
                label = { Text(text = screen.title!!)},
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route)
                }
            )
        }
    }
}