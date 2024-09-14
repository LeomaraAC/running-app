package com.example.runningapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.ui.navigation.NavigationDestination
import com.example.runningapp.ui.screens.components.SettingsInputForm
import com.example.runningapp.ui.theme.RunningAppTheme
import com.example.runningapp.ui.viewmodels.SettingsViewModel


object WelcomeDestination : NavigationDestination {
    override val route: String = "welcome"
    override val icon = null
    override val title = null
}

@Composable
fun SetupScreen(
    navigateToRun: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel()
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer) {
        Column(
            modifier.padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_message),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(20.dp))
                SettingsInputForm(
                    settingsUiState = viewModel.settingsUiState,
                    onValueChange = viewModel::updateUiState,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ElevatedButton(
                onClick = navigateToRun,
                enabled = viewModel.settingsUiState.isValid,
                modifier = Modifier.widthIn(min = 150.dp)
            ) {
                Text(text = stringResource(id = R.string.continuar))
                Spacer(modifier = Modifier.width(5.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun SetupScreenPreview() {
    RunningAppTheme() {
        SetupScreen(
            navigateToRun = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}