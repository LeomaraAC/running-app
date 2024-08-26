package com.example.runningapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.runningapp.ui.screens.RunDestination
import com.example.runningapp.ui.screens.RunScreen
import com.example.runningapp.ui.screens.SettingsDestination
import com.example.runningapp.ui.screens.SettingsScreen
import com.example.runningapp.ui.screens.SetupDestination
import com.example.runningapp.ui.screens.SetupScreen
import com.example.runningapp.ui.screens.StatisticsDestination
import com.example.runningapp.ui.screens.StatisticsScreen
import com.example.runningapp.ui.screens.TrackingDestination
import com.example.runningapp.ui.screens.TrackingScreen
import com.example.runningapp.ui.viewmodels.MainViewModel
import com.example.runningapp.ui.viewmodels.StatisticsViewModel

@Composable
fun RunNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = SetupDestination.route,
        modifier = modifier
    ) {
        composable(route = SetupDestination.route) {
            SetupScreen()
        }

        composable(route = RunDestination.route) {
            val viewModel = hiltViewModel<MainViewModel>()
            RunScreen(viewModel = viewModel)
        }

        composable(route = StatisticsDestination.route) {
            val viewModel = hiltViewModel<StatisticsViewModel>()
            StatisticsScreen(viewModel = viewModel)
        }

        composable(route = SettingsDestination.route) {
            SettingsScreen()
        }

        composable(route = TrackingDestination.route) {
            val viewModel = hiltViewModel<MainViewModel>()
            TrackingScreen(viewModel = viewModel)
        }
    }
}