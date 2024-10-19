package com.example.runningapp.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.MainViewModel
import com.google.maps.android.compose.GoogleMap

object TrackingDestination: NavigationDestination {
    override val route: String = "tracking"
    override val icon = null
    override val title = null
}

@Composable
fun TrackingScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primaryContainer), horizontalAlignment = Alignment.CenterHorizontally) {
        GoogleMap(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) { }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.default_space)))
        Text(
            text = "0:00:00",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        ElevatedButton(
            onClick = {},
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.comecar_corrida))
            Spacer(modifier = Modifier.width(5.dp))
            Icon(painter = painterResource(id = R.drawable.directions_run_icon), contentDescription = null)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun TrackingScreenPreview() {
    RunningAppTheme {
        TrackingScreen()
    }
}

