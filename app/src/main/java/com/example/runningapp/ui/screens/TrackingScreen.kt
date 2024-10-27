package com.example.runningapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.runningapp.services.TrackingService
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.viewmodels.MainViewModel
import com.example.runningapp.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.GoogleMap

object TrackingDestination : NavigationDestination {
    override val route: String = "tracking"
    override val icon = null
    override val title = null
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TrackingScreen(
    context: Context,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val postNotificationPermissions =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

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
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) { }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.default_space)))
        Text(
            text = "0:00:00",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        StartButton(context = context)
    }
}

@Composable
fun StartButton(context: Context, modifier: Modifier = Modifier) {
    ElevatedButton(
        onClick = {
            sendCommandToService(
                context = context,
                action = ACTION_START_OR_RESUME_SERVICE
            )
        },
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.comecar_corrida))
        Spacer(modifier = Modifier.width(5.dp))
        Icon(
            painter = painterResource(id = R.drawable.directions_run_icon),
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
