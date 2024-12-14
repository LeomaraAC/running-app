package com.example.runningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.services.Polylines
import com.example.runningapp.services.TrackingService
import com.example.runningapp.utils.TrackingUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrackingViewModel : ViewModel() {
    private val _pathPoints = MutableStateFlow<Polylines>(mutableListOf())
    private val _isTracking = MutableStateFlow(false)
    private val _curTimeFormatted = MutableStateFlow("00:00:00:00")

    val pathPoints: StateFlow<Polylines> get() = _pathPoints
    val isTracking: StateFlow<Boolean> get() = _isTracking
    val curTimeFormatted: StateFlow<String> get() = _curTimeFormatted

    init {
        viewModelScope.launch {
            TrackingService.pathPoints.collect { newPathPoints ->
                _pathPoints.value = newPathPoints
            }
        }

        viewModelScope.launch {
            TrackingService.isTracking.collect { _isTracking.value = it }
        }

        viewModelScope.launch {
            TrackingService.timeRunInMillis.collect {
                _curTimeFormatted.value = TrackingUtility.getFormattedStopWatchTime(it, true)
            }
        }
    }
}