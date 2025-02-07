package com.example.gardenbuddy.ui.screens.homescreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gardenbuddy.data.models.User

class GardeningMonitoringViewModelFactory(
    private val application: Application,
    private val user: User
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GardeningMonitoringViewModel::class.java)) {
            return GardeningMonitoringViewModel(application, user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
