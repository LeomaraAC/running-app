package com.example.runningapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.screens.components.SimpleDialog
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val title: String = "Suas corridas"
    override val icon: Int
        get() = R.drawable.directions_run_icon
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RunScreen(
    navigateToStartTracking: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val fineLocationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val backgroundLocationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    LaunchedEffect(
        key1 = !fineLocationPermissionState.allPermissionsGranted,
        key2 = !backgroundLocationPermissionState.status.isGranted
    ) {
        askPermission(fineLocationPermissionState, backgroundLocationPermissionState)
    }

    Scaffold(
        floatingActionButton = {
            FloatingButton(
                navigateTo = navigateToStartTracking,
                fineLocationPermissionState = fineLocationPermissionState,
                backgroundLocationPermissionState = backgroundLocationPermissionState
            )
        },
        modifier = modifier
    ) {
        Text(text = "Home")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FloatingButton(
    navigateTo: () -> Unit,
    fineLocationPermissionState: MultiplePermissionsState,
    backgroundLocationPermissionState: PermissionState,
    modifier: Modifier = Modifier
) {
    var openAlertDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {
            if (!fineLocationPermissionState.allPermissionsGranted || !backgroundLocationPermissionState.status.isGranted) {
                openAlertDialog = true
            } else {
                navigateTo()
            }
        },
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Começar corrida")
    }

    if (openAlertDialog) {
        SimpleDialog(
            title = "Permissão",
            text = "Para que o aplicativo funcione corretamente é necessário fornecer a permissão para acessar a localização",
            confirmText = "Ok"
        ) {
            openAlertDialog = false
            askPermission(fineLocationPermissionState, backgroundLocationPermissionState)
        }
    }

}


@OptIn(ExperimentalPermissionsApi::class)
fun askPermission(
    fineLocationPermissionState: MultiplePermissionsState,
    backgroundLocationPermissionState: PermissionState,
) {
    if (!fineLocationPermissionState.allPermissionsGranted) {
        fineLocationPermissionState.launchMultiplePermissionRequest()
    } else if (!backgroundLocationPermissionState.status.isGranted) {
        backgroundLocationPermissionState.launchPermissionRequest()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RunScreenPreview() {
    RunningAppTheme {
        RunScreen(navigateToStartTracking = {})
    }
}
