package com.example.runningapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.MainViewModel

object TrackingDestination: NavigationDestination {
    override val route: String = "tracking"
    override val icon = null
    override val title = null
}

@Composable
fun TrackingScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Text(text = "Tracking")
}

@Preview(showBackground = true)
@Composable
fun TrackingScreenPreview() {
    RunningAppTheme {
        TrackingScreen()
    }
}

