package com.example.gardenbuddy.ui.screens.plantScreen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.data.repositories.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlantScreenViewModel : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _plantLoadSuccess = MutableStateFlow<Plant?>(null)
    val plantLoadSuccess = _plantLoadSuccess.asStateFlow()

    // implement this stuffs in the repository file

    fun loadPlant(PlantId : Long){

        viewModelScope.launch {
            _isLoading.value = true
            val result = PlantRepository.loadPlant(PlantId)
            _isLoading.value = false

            result.onSuccess { plant ->
                _plantLoadSuccess.value = plant
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }

    }

    // there isn't the add, because is handled automatically in the BE

    fun searchPlant(photo : Bitmap){

    }

    fun searchPlant(name : String){

    }


}