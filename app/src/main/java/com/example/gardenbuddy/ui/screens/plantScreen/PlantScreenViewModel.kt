package com.example.gardenbuddy.ui.screens.plantScreen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.data.repositories.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class PlantScreenViewModel : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _plantLoadSuccess = MutableStateFlow<Plant?>(null)
    val plantLoadSuccess = _plantLoadSuccess.asStateFlow()

    private val _plantSearchSuccess = MutableStateFlow<List<Plant>?>(emptyList())
    val plantSearchSuccess = _plantSearchSuccess.asStateFlow()



    // there isn't the add, because is handled automatically in the BE

    fun clearSearchResults(){
        _plantSearchSuccess.value = emptyList()

    }

    fun searchPlant(photo : String, latitude : Double, longitude : Double){
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            val result = PlantRepository.searchPlant(photo, latitude, longitude)

            _isLoading.value = false

            result.onSuccess { plant ->
                _plantSearchSuccess.value = plant
            }.onFailure { error ->
                _plantSearchSuccess.value = null
                val errorMessage = error.message ?: ""
                if (errorMessage.startsWith("Error: ")) {
                    try {
                        val jsonString = errorMessage.substring(7) // Remove "Error: " prefix
                        val errorJson = JSONObject(jsonString)
                        _errorMessage.value = errorJson.getString("error")
                    } catch (e: Exception) {
                        _errorMessage.value = error.message ?: "Unknown error"
                    }
                } else {
                    _errorMessage.value = error.message ?: "Unknown error"
                }
            }
        }

    }

    fun searchPlant(name : String){
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            val result = PlantRepository.searchPlant(name)
            _isLoading.value = false

            result.onSuccess { plant ->
                _plantSearchSuccess.value = plant
            }.onFailure { error ->
                _plantSearchSuccess.value = null

                val errorMessage = error.message ?: ""
                if (errorMessage.startsWith("Error: ")) {
                    try {
                        val jsonString = errorMessage.substring(7) // Remove "Error: " prefix
                        val errorJson = JSONObject(jsonString)
                        _errorMessage.value = errorJson.getString("error")
                    } catch (e: Exception) {
                        _errorMessage.value = error.message ?: "Unknown error"
                    }
                } else {
                    _errorMessage.value = error.message ?: "Unknown error"
                }
            }
        }

    }


}