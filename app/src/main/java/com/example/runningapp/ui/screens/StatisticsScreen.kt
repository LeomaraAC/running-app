package com.example.runningapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.StatisticsViewModel

object StatisticsDestination: NavigationDestination {
    override val route: String = "statistics"
    override val title: String = "Estatísticas"
    override val icon: Int
        get() = R.drawable.show_chart_icon
}

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = viewModel()
) {
    Text(text = "Statistics")
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    RunningAppTheme {
        StatisticsScreen()
    }
}
