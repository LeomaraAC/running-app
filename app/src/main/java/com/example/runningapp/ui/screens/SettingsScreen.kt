package com.example.runningapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.runningapp.R
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme

object SettingsDestination: NavigationDestination {
    override val route: String = "settings"
    override val title: String = "Configurações"
    override val icon: Int
        get() = R.drawable.settings_icon
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Text(text = "Settings")
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    RunningAppTheme {
        SettingsScreen()
    }
}
