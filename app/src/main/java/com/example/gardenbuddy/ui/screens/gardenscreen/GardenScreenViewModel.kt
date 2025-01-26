package com.example.gardenbuddy.ui.screens.gardenscreen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.data.models.GardenPlant
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.data.repositories.AuthRepository
import com.example.gardenbuddy.data.repositories.GardenPlantRepository
import com.example.gardenbuddy.data.repositories.GardenRepository
import com.example.gardenbuddy.data.repositories.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GardenScreenViewModel : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _gardenPlantsisLoading = MutableStateFlow(true)
    val gardenPlantsisLoading = _gardenPlantsisLoading.asStateFlow()

    private val _gardenplantsLoadSuccess = MutableStateFlow<List<Pair<Plant, List<String>>>?>(null)
    val gardenplantsLoadSuccess = _gardenplantsLoadSuccess.asStateFlow()

    private val _gardenplantLoadSuccess = MutableStateFlow<Pair<Plant, List<String>>?>(null)
    val gardenplantLoadSuccess = _gardenplantLoadSuccess.asStateFlow()


    private val _gardenLoadSuccess = MutableStateFlow<Garden?>(null)
    val gardenLoadSuccess = _gardenLoadSuccess.asStateFlow()

    private val _gardenSaveSuccess = MutableStateFlow<Garden?>(null)
    val gardenSaveSuccess = _gardenSaveSuccess.asStateFlow()

    private val _gardenDeleteSuccess = MutableStateFlow<String?>(null)
    val gardenDeleteSuccess = _gardenDeleteSuccess.asStateFlow()


    private val _plantRemoveSuccess = MutableStateFlow<String?>(null)
    val plantRemoveSuccess = _plantRemoveSuccess.asStateFlow()

    private val _plantAddSuccess = MutableStateFlow<GardenPlant?>(null)
    val plantAddSuccess = _plantAddSuccess.asStateFlow()




    fun loadGardenPlants(gardenId : Long){
        _gardenPlantsisLoading.value = true
        // now charge the Gardenplants
        viewModelScope.launch {
            val result = GardenPlantRepository.loadGardenPlantsById(gardenId)
            println(result)

            result.onSuccess { List ->
                _gardenplantsLoadSuccess.value = List
                _gardenPlantsisLoading.value = false
            }.onFailure { error ->
                _gardenPlantsisLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun loadGardenPlant(plantId: Long, gardenId: Long){
        _gardenPlantsisLoading.value = true
        viewModelScope.launch {
            val result = GardenPlantRepository.loadGardenPlant(plantId, gardenId)

            result.onSuccess { plant ->
                _gardenplantLoadSuccess.value = plant
                _gardenPlantsisLoading.value = false
            }.onFailure { error ->
                _gardenPlantsisLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun  removeAllPlants(gardenId : Long){
        _gardenPlantsisLoading.value = true
        viewModelScope.launch {
            val result = GardenPlantRepository.removeAllPlants(gardenId)

            result.onSuccess { result ->
                _plantRemoveSuccess.value = result
                _gardenPlantsisLoading.value = false
            }.onFailure { error ->
                _gardenPlantsisLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun loadGarden(userId : String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = GardenRepository.loadGarden(userId)
            result.onSuccess { garden ->
                _gardenLoadSuccess.value = garden
                _isLoading.value = false
            }.onFailure { error ->
                _isLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun loadGardenByGardenId(gardenId : Long){
        _isLoading.value = true
        viewModelScope.launch {
            val result = GardenRepository.loadGardenByGardenId(gardenId)
            result.onSuccess { garden ->
                println(garden)
                _gardenLoadSuccess.value = garden
                _isLoading.value = false
            }.onFailure { error ->
                _isLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun saveGarden(garden : Garden){
        _isLoading.value = true
        viewModelScope.launch {
            val result = GardenRepository.saveGarden(garden)
            result.onSuccess { garden ->
                _gardenLoadSuccess.value = garden
                _isLoading.value = false
            }.onFailure { error ->
                _isLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun updateGarden(gardenId : Long, garden : Garden){
        _isLoading.value = true
        viewModelScope.launch {
            val result = GardenRepository.updateGarden(gardenId, garden)
            result.onSuccess { garden ->
                _gardenLoadSuccess.value = garden
                _isLoading.value = false
            }.onFailure { error ->
                _isLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    /*fun deleteGarden(gardenId : Long){
        _isLoading.value = true
        viewModelScope.launch {
            val result = GardenRepository.deleteGarden(gardenId)
            result.onSuccess { message ->
                _gardenDeleteSuccess.value = message
                _isLoading.value = false
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }*/

    fun removePlant(plantId : Long, gardenId : Long){
        _gardenPlantsisLoading.value = true
        viewModelScope.launch {
            val result = GardenPlantRepository.removePlant(plantId, gardenId)
            result.onSuccess { message ->
                _plantRemoveSuccess.value = message
                _gardenPlantsisLoading.value = false
            }.onFailure { error ->
                _gardenPlantsisLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun addPlant(plantId : Long, gardenId : Long, photos : List<String>){
        _gardenPlantsisLoading.value = true
        viewModelScope.launch {
            val result = GardenPlantRepository.addPlant(plantId, gardenId, photos)
            result.onSuccess { gardenPlant ->
                val plantResult = PlantRepository.loadPlant(plantId)
                plantResult.onSuccess { plant ->
                    println(_gardenplantsLoadSuccess.value)
                    _gardenplantsLoadSuccess.value = _gardenplantsLoadSuccess.value.orEmpty() + Pair(plant, photos)
                    println(_gardenplantsLoadSuccess.value)
                    _plantAddSuccess.value = gardenPlant
                    _gardenPlantsisLoading.value = false
                }.onFailure { error ->
                    _gardenPlantsisLoading.value = false
                    _errorMessage.value = error.message ?: "An error occurred"}
            }.onFailure { error ->
                _gardenPlantsisLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"

            }
        }
    }








}

