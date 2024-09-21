package com.example.runningapp.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.runningapp.R
import com.example.runningapp.ui.entities.Settings
import com.example.runningapp.ui.viewmodels.SettingsUiState


@Composable
fun SettingsInputForm(
    settingsUiState: SettingsUiState,
    onValueChange: (Settings) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = settingsUiState.settings.name,
            onValueChange = { onValueChange(settingsUiState.settings.copy(name = it)) },
            label = { Text(text = stringResource(id = R.string.settings_nome_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
        )
        OutlinedTextField(
            value = settingsUiState.settings.weight,
            onValueChange = { onValueChange(settingsUiState.settings.copy(weight = it)) },
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.settings_peso_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
    }
}