package com.example.gardenbuddy.data.repositories

import android.graphics.Bitmap
import com.example.gardenbuddy.data.models.GardenPlant
import com.example.gardenbuddy.data.models.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow



object GardenPlantRepository {

    /*private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()


    private val _gardenplantLoadSuccess = MutableStateFlow<Pair<Plant, List<Bitmap>>?>(null)
    val gardenplantLoadSuccess = _gardenplantLoadSuccess.asStateFlow()*/


    suspend fun loadGardenPlant(PlantId: Long, GardenId: Long): Result<Pair<Plant, List<Bitmap>>> {
        val result = PlantRepository.loadPlant(PlantId);

        return try {
            var resultOutput: Result<Pair<Plant, List<Bitmap>>> = Result.failure(Exception("Unknown error"))

            result.onSuccess { plant ->
                val photos: List<Bitmap> = emptyList() // retrieve photos from the couple PlantId, GardenId in the GardenPlant db
                val output = Pair(plant, photos)

                resultOutput = Result.success(output)

                //_gardenplantLoadSuccess.value = output
            }.onFailure { error ->
                throw Exception(error.localizedMessage ?: "Unknown error")
                //_errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
            resultOutput

        }catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun loadGardenPlants(gardenId : Long) : Result<List<Pair<Plant, List<Bitmap>>>> {

        val results = listOf(PlantRepository.loadPlant(123))

        return try {
            var resultOutput: Result<List<Pair<Plant, List<Bitmap>>>> = Result.failure(Exception("Unknown error"))
            val outputList = mutableListOf<Pair<Plant, List<Bitmap>>>()

            results.stream().forEach { result ->
                run {
                    result.onSuccess { plant ->
                        val photos: List<Bitmap> =
                            emptyList() // retrieve photos from the couple PlantId, GardenId in the GardenPlant db
                        val output = Pair(plant, photos)
                        outputList.add(output)

                        //_gardenplantLoadSuccess.value = output
                    }.onFailure { error ->
                        throw Exception(error.localizedMessage ?: "Unknown error")
                        //_errorMessage.value = error.localizedMessage ?: "Unknown error"
                    }

                }

            }
            resultOutput = Result.success(outputList)
            resultOutput

        }catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun removePlant(plantId : Long, gardenId : Long) : Result<String> {
        // TODO implement
        return Result.success("Plant removed")

    }

    suspend fun removeAllPlants(gardenId : Long) : Result<String> {
        // TODO implement
        return Result.success("Plants removed")

    }

    suspend fun addPlant(plantId : Long, gardenId : Long, photos : List<Bitmap>) : Result<GardenPlant> {
        // TODO implement the save on the PlantGarden
        return Result.success(GardenPlant(photos, gardenId, plantId))
    }

}
