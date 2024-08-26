package com.example.runningapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme

object SettingsDestination: NavigationDestination {
    override val route: String = "settings"
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    RunningAppTheme {
        SettingsScreen()
    }
}
