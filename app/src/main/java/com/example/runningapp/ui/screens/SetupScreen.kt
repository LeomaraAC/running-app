package com.example.runningapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme


object SetupDestination: NavigationDestination {
    override val route: String = "setup"
}

@Composable
fun SetupScreen(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun SetupScreenPreview() {
    RunningAppTheme {
        SetupScreen()
    }
}