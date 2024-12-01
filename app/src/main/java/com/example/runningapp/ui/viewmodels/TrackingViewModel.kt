package com.example.runningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.services.Polylines
import com.example.runningapp.services.TrackingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrackingViewModel: ViewModel() {
    private val _pathPoints = MutableStateFlow<Polylines>(mutableListOf())
    private val _isTracking = MutableStateFlow(false)
    val pathPoints: StateFlow<Polylines> get() = _pathPoints
    val isTracking: StateFlow<Boolean> get() = _isTracking

    init {
        viewModelScope.launch {
            TrackingService.pathPoints.collect { newPathPoints ->
                _pathPoints.value = newPathPoints
            }
        }

        viewModelScope.launch {
            TrackingService.isTracking.collect {_isTracking.value = it}
        }
    }
}