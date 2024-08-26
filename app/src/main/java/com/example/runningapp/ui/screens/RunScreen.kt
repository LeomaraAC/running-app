package com.example.runningapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.MainViewModel

object RunDestination: NavigationDestination {
    override val route: String = "run"
}

@Composable
fun RunScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
   
}


@Preview(showBackground = true)
@Composable
fun RunScreenPreview() {
    RunningAppTheme {
        RunScreen()
    }
}
