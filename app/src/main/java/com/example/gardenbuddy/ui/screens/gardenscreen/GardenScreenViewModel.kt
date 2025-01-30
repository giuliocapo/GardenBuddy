package com.example.gardenbuddy.ui.screens.gardenscreen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.data.models.GardenPlant
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.data.repositories.GardenPlantRepository
import com.example.gardenbuddy.data.repositories.GardenRepository
import com.example.gardenbuddy.data.repositories.PlantRepository
import com.example.gardenbuddy.utils.LocationUtils
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

    private val _plantRemoveSuccess = MutableStateFlow<String?>(null)
    val plantRemoveSuccess = _plantRemoveSuccess.asStateFlow()

    private val _plantAddSuccess = MutableStateFlow<GardenPlant?>(null)
    val plantAddSuccess = _plantAddSuccess.asStateFlow()

    private val _gardenplantsSuccess = MutableLiveData<List<Pair<Plant, List<String>>>?>(emptyList())
    val gardenplantsSuccess: LiveData<List<Pair<Plant, List<String>>>?> = _gardenplantsSuccess

    private val _gardencoordinatesSuccess = MutableLiveData<Pair<Double, Double>?>()
    val gardencoordinatesSuccess: LiveData<Pair<Double, Double>?> = _gardencoordinatesSuccess

    private val _isLoadingCoordinates = MutableStateFlow(false)
    val isLoadingCoordinates = _isLoadingCoordinates.asStateFlow()

    private val _hasLocationPermission = MutableStateFlow(false)
    val hasLocationPermission: MutableStateFlow<Boolean> = _hasLocationPermission

    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: MutableStateFlow<Pair<Double, Double>?> = _currentLocation



    /**
     * Request location permissions and update state based on the result.
     */
    fun requestLocationPermission(
        permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        LocationUtils.requestLocationPermissions(permissionLauncher)
    }

    /**
     * Update permission status when the result is received.
     */
    fun updatePermissionStatus(isGranted: Boolean) {
        _hasLocationPermission.value = isGranted
    }

    /**
     * Fetch the current location if permissions are granted.
     */
    fun fetchCurrentLocation(activity: ComponentActivity) {
        viewModelScope.launch {
            if (_hasLocationPermission.value) {
                val location = LocationUtils.getCurrentLocation(activity)
                _currentLocation.value = location
            }
        }
    }

    fun checkInitialPermission(activity: ComponentActivity) {
        val isGranted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        _hasLocationPermission.value = isGranted
    }




    fun loadGardenPlants(gardenId : Long){
        _gardenPlantsisLoading.value = true
        viewModelScope.launch {
            val result = GardenPlantRepository.loadGardenPlantsById(gardenId)
            result.onSuccess { List ->
                _gardenplantsSuccess.value = List
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

    fun updateGardenPlant(gardenId : Long, plantId : Long, photos : List<String>){

        _isLoading.value = true
        viewModelScope.launch {
            val result = GardenPlantRepository.updateGardenPlant(gardenId, plantId, photos)
            result.onSuccess { gardenPlant ->
                _gardenplantsSuccess.value = _gardenplantsSuccess.value?.map { pair ->
                    if (pair.first.plantId == plantId) {
                        // Update the photos for the matching plant
                        pair.copy(second = gardenPlant.photos)
                    } else {
                        pair // Keep other plants unchanged
                    }
                }
                _isLoading.value = false
            }.onFailure { error ->
                _isLoading.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun getGardenCoordinates(gardenId : Long){
        _isLoadingCoordinates.value = true
        viewModelScope.launch {
            val result = GardenRepository.loadGardenByGardenId(gardenId)
            result.onSuccess { garden ->
                _gardencoordinatesSuccess.value = Pair(garden.latitude, garden.longitude)
                _isLoadingCoordinates.value = false
            }.onFailure { error ->
                _isLoadingCoordinates.value = false
                _errorMessage.value = error.message ?: "An error occurred"
            }
        }
    }

    fun removePlant(plantId : Long, gardenId : Long){
        // TODO understand why the ui doesn't update the gardenplantsLoadSuccess
        _gardenPlantsisLoading.value = true
        viewModelScope.launch {
            val result = GardenPlantRepository.removePlant(plantId, gardenId)
            result.onSuccess { message ->
                val updatedPlants = _gardenplantsSuccess.value?.filterNot { it.first.plantId == plantId }
                _gardenplantsSuccess.value = updatedPlants
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
                    //_gardenplantsLoadSuccess.value = _gardenplantsLoadSuccess.value.orEmpty() + Pair(plant, photos)
                    _gardenplantsSuccess.value = _gardenplantsSuccess.value.orEmpty() + Pair(plant, photos)
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

