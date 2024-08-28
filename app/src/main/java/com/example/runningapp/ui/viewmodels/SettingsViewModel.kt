package com.example.runningapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.runningapp.ui.entities.Settings

class SettingsViewModel : ViewModel() {
    var settingsUiState by mutableStateOf(SettingsUiState())
        private set

    fun updateUiState(settings: Settings) {
        settingsUiState = SettingsUiState(settings = settings, isValid = validateInputs(settings))
    }

    private fun validateInputs(settings: Settings = settingsUiState.settings): Boolean {
        return with(settings) {
            name.isNotBlank() && (weight.replace(',', '.').toFloatOrNull()?.let { it > 10f} ?: false)
        }
    }
}

data class SettingsUiState(
    val settings: Settings = Settings(),
    val isValid: Boolean = false
)