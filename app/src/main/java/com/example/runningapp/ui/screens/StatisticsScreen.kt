package com.example.runningapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.StatisticsViewModel

object StatisticsDestination: NavigationDestination {
    override val route: String = "statistics"
}

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = viewModel()
) {}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    RunningAppTheme {
        StatisticsScreen()
    }
}
