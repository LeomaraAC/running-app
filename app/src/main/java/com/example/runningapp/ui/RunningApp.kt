package com.example.runningapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.runningapp.ui.navigation.RunNavHost

@Composable
fun RunningApp(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController()
) {
    RunNavHost(navController = navHostController, modifier = modifier)
}