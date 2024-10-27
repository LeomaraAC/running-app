package com.example.runningapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.runningapp.ui.screens.HomeDestination
import com.example.runningapp.ui.screens.RunScreen
import com.example.runningapp.ui.screens.SettingsDestination
import com.example.runningapp.ui.screens.SettingsScreen
import com.example.runningapp.ui.screens.WelcomeDestination
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
        startDestination = WelcomeDestination.route,
        modifier = modifier
    ) {
        composable(route = WelcomeDestination.route) {
            SetupScreen(navigateToRun = { navController.navigate(HomeDestination.route) })
        }

        composable(route = HomeDestination.route) {
            val viewModel = hiltViewModel<MainViewModel>()
            RunScreen(
                navigateToStartTracking = { navController.navigate(TrackingDestination.route) },
                viewModel = viewModel
            )
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
            val context = LocalContext.current
            TrackingScreen(context=context, viewModel = viewModel)
        }
    }
}