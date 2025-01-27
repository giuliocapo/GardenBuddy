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

    private val _plantSearchSuccess = MutableStateFlow<List<Plant>?>(null)
    val plantSearchSuccess = _plantSearchSuccess.asStateFlow()



    // implement this stuffs in the repository file

    /*fun loadPlant(PlantId : Long){

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

    }*/

    // there isn't the add, because is handled automatically in the BE

    fun clearSearchResults(){
        _plantSearchSuccess.value = emptyList()

    }

    fun searchPlant(photo : Bitmap){
        viewModelScope.launch {
            _isLoading.value = true
            val result = PlantRepository.searchPlant(photo.toString()) // TODO modify this
            _isLoading.value = false

            result.onSuccess { plant ->
                _plantSearchSuccess.value = plant
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }

    }

    fun searchPlant(name : String){
        viewModelScope.launch {
            _isLoading.value = true
            val result = PlantRepository.searchPlant(name)
            _isLoading.value = false

            result.onSuccess { plant ->
                _plantSearchSuccess.value = plant
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }

    }


}