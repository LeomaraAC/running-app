package com.example.runningapp.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.MainViewModel

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val title: String = "Suas corridas"
    override val icon: Int
        get() = R.drawable.directions_run_icon
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RunScreen(
    navigateToStartTracking: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        floatingActionButton = { FloatingButton(navigateTo = navigateToStartTracking) },
        modifier = modifier
    ) {
        Text(text = "Home")
    }
}

@Composable
fun FloatingButton(navigateTo: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(onClick = { navigateTo() }, shape = MaterialTheme.shapes.extraLarge) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Começar corrida")
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RunScreenPreview() {
    RunningAppTheme {
        RunScreen(navigateToStartTracking = {})
    }
}
