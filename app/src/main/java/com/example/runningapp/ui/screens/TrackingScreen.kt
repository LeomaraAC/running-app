package com.example.runningapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.services.Polylines
import com.example.runningapp.services.TrackingService
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.viewmodels.MainViewModel
import com.example.runningapp.ui.viewmodels.TrackingViewModel
import com.example.runningapp.utils.Constants.ACTION_PAUSE_SERVICE
import com.example.runningapp.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningapp.utils.Constants.ACTION_STOP_SERVICE
import com.example.runningapp.utils.Constants.MAP_ZOOM
import com.example.runningapp.utils.Constants.POLYLINE_COLOR
import com.example.runningapp.utils.Constants.POLYLINE_WIDTH
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

object TrackingDestination : NavigationDestination {
    override val route: String = "tracking"
    override val icon = null
    override val title = null
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TrackingScreen(
    context: Context,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    trackingViewModel: TrackingViewModel = viewModel()
) {

    val postNotificationPermissions =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val pathPoints by trackingViewModel.pathPoints.collectAsState(initial = mutableListOf())
    val isTracking by trackingViewModel.isTracking.collectAsState(initial = false)
    val timeRunning by trackingViewModel.curTimeFormatted.collectAsState(initial = "00:00:00:00")
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    val cameraPositionState = rememberCameraPositionState()
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN, isMyLocationEnabled = true))
    }

    LaunchedEffect(pathPoints) {
        if (pathPoints.size == 0) {
            return@LaunchedEffect
        }
        val currentPosition = trackingViewModel.getCurrentPosition(pathPoints)
        if (pathPoints.size == 1 && pathPoints.last().size == 0) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, MAP_ZOOM)
        } else {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(currentPosition),
                durationMs = 1000
            )
        }
    }

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermissions.status.isGranted) {
            postNotificationPermissions.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isTracking || pathPoints.size > 0) {
            Row(modifier = Modifier.fillMaxWidth()) {
                CancelRunButton(
                    context = context,
                    navigateToHome = navigateToHome,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = properties,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            pathPoints.forEach { polylines ->
                Polyline(
                    points = polylines,
                    clickable = true,
                    color = POLYLINE_COLOR,
                    width = POLYLINE_WIDTH
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.default_space)))
        Text(
            text = timeRunning,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            if (isTracking) {
                PauseButton(context = context, modifier = Modifier.weight(1f))
            } else {
                StartButton(context = context, modifier = Modifier.weight(1f))
                if (pathPoints.size > 0) {
                    StopButton(context = context, modifier = Modifier.weight(1f)) {
                        zoomToSeeWholeTrack(pathPoints, cameraPositionState)
                        endAndSaveToDb(trackingViewModel, googleMap)
                    }
                }
            }
        }


    }
}

@Composable
fun StartButton(context: Context, modifier: Modifier = Modifier) {
    BaseButton(
        text = R.string.comecar_corrida,
        icon = R.drawable.directions_run_icon,
        modifier = modifier
    ) {
        sendCommandToService(
            context = context,
            action = ACTION_START_OR_RESUME_SERVICE
        )
    }

}

@Composable
fun PauseButton(context: Context, modifier: Modifier = Modifier) {
    BaseButton(
        text = R.string.pausar_corrida,
        icon = R.drawable.pause_icon,
        modifier = modifier
    ) {
        sendCommandToService(
            context = context,
            action = ACTION_PAUSE_SERVICE
        )
    }
}

@Composable
fun CancelRunButton(context: Context, navigateToHome: () -> Unit, modifier: Modifier = Modifier) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        CancelarCorridaDialog(
            confirmButton = {
                sendCommandToService(
                    context = context,
                    action = ACTION_STOP_SERVICE
                )
                navigateToHome()
            },
            setShowDialog = { showDialog.value = it }
        )
    }
    BaseButton(
        text = R.string.cancelar_corrida,
        icon = R.drawable.close_icon,
        modifier = modifier
    ) {
        showDialog.value = true
    }
}

@Composable
fun StopButton(context: Context, modifier: Modifier = Modifier, saveAction: () -> Unit) {
    BaseButton(
        text = R.string.finalizar_corrida,
        icon = R.drawable.stop_icon,
        modifier = modifier
    ) {
        sendCommandToService(
            context = context,
            action = ACTION_STOP_SERVICE
        )
        saveAction()
    }
}

@Composable
fun CancelarCorridaDialog(
    confirmButton: () -> Unit = {},
    cancelButton: () -> Unit = {},
    setShowDialog: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        icon = { Icon(Icons.Filled.Close, null) },
        title = { Text(text = stringResource(id = R.string.cancelar_corrida_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.cancelar_corrida_dialog_message)) },
        onDismissRequest = { setShowDialog(false) },
        confirmButton = {
            ElevatedButton(onClick = {
                confirmButton()
                setShowDialog(false)
            }) {
                Text(text = stringResource(id = R.string.cancelar_corrida_dialog_confirm))
            }
        },
        dismissButton = {
            ElevatedButton(onClick = {
                cancelButton()
                setShowDialog(false)
            }) {
                Text(text = stringResource(id = R.string.cancelar_corrida_dialog_cancel))
            }
        },
    )

}

@Composable
private fun BaseButton(
    @StringRes text: Int,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = text))
        Spacer(modifier = Modifier.width(5.dp))
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

private fun sendCommandToService(context: Context, action: String) {
    Intent(context, TrackingService::class.java).also {
        it.action = action
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(it)
        } else {
            context.startService(it)
        }
    }
}

private fun zoomToSeeWholeTrack(pathPoints: Polylines, cameraPositionState: CameraPositionState) {
    val bounds = LatLngBounds.Builder()
    pathPoints.forEach { polyline ->
        polyline.forEach { latLng ->
            bounds.include(latLng)
        }
    }
    cameraPositionState.move(
        CameraUpdateFactory.newLatLngBounds(bounds.build(), 100)
    )
}

private fun endAndSaveToDb(viewModel: TrackingViewModel, googleMap: GoogleMap?) {
    googleMap?.snapshot { bitmap ->
        viewModel.saveRun(bitmap)
    }
}
