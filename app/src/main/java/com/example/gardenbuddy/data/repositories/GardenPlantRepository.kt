package com.example.gardenbuddy.data.repositories

import com.example.gardenbuddy.data.Dtos.requests.CreategardenplantDTO
import com.example.gardenbuddy.data.Dtos.requests.UpdateGardenPlantRequest
import com.example.gardenbuddy.data.models.GardenPlant
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.utils.RetrofitClient


object GardenPlantRepository {

    suspend fun loadGardenPlant(plantId: Long, gardenId: Long): Result<Pair<Plant, List<String>>> {
        return try {
            val response = RetrofitClient.gardenPlantApiService.getGardenPlantByGardenIdAndPlantId(gardenId, plantId)
            if (response.isSuccessful) {
                val gardenplant = response.body()
                if (gardenplant != null) {
                    val p_response = RetrofitClient.plantApiService.getPlantById(plantId)
                    if (!p_response.isSuccessful) {
                        return Result.failure(Exception("Plant data is null"))
                    }
                    val plant = p_response.body() ?: return Result.failure(Exception("Plant data is null"))

                    val photos = gardenplant.photos
                    Result.success(Pair(plant, photos))

                } else {
                    Result.failure(Exception("GardenPlant data is null"))
                }
            } else {
                Result.failure(Exception(Exception("Error: ${response.body() ?: ""}")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadGardenPlantsById(gardenId : Long) : Result<List<Pair<Plant, List<String>>>> {

        return try {
            val response = RetrofitClient.gardenPlantApiService.getGardenPlantsByGardenId(gardenId)
            if (response.isSuccessful && response.body() != null) {
                val plants = mutableListOf<Pair<Plant, List<String>>>()
                for (gardenplant in response.body()!!) {
                    val p_response = RetrofitClient.plantApiService.getPlantById(gardenplant.plantId)
                    if (!p_response.isSuccessful) {
                        return Result.failure(Exception("Plant data is null"))
                    }
                    val plant = p_response.body() ?: return Result.failure(Exception("Plant data is null"))
                    val photos = gardenplant.photos
                    plants.add(Pair(plant, photos))
                }
                Result.success(plants)

            } else {
                Result.failure(Exception(Exception("Error: ${response.body() ?: ""}")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    suspend fun updateGardenPlant(gardenId : Long, plantId : Long, photos : List<String>) : Result<GardenPlant> {

        return try {
            val body = UpdateGardenPlantRequest(photos)
            val response = RetrofitClient.gardenPlantApiService.updateGardenPlant(gardenId, plantId, body)
            if (response.isSuccessful) {
                val plant = response.body()?.entity
                if (plant != null) {
                    Result.success(plant)
                } else {
                    Result.failure(Exception("Plant data is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun removePlant(plantId : Long, gardenId : Long) : Result<String> {
        return try {
            val response = RetrofitClient.gardenPlantApiService.deleteGardenPlant(gardenId, plantId)
            if (response.isSuccessful) {
                val message = response.body()?.message
                if (message != null) {
                    Result.success(message)
                } else {
                    Result.failure(Exception("Delete message is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.body()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun removeAllPlants(gardenId : Long) : Result<String> {
        // TODO implement (if needed)
        return Result.success("Plants removed")

    }

    suspend fun addPlant(plantId : Long, gardenId : Long, photos : List<String>) : Result<GardenPlant> {
        val convPhotos = photos
        val dto = CreategardenplantDTO(convPhotos, gardenId, plantId)
        return try {
            val response = RetrofitClient.gardenPlantApiService.createGardenPlant(dto)
            if (response.isSuccessful) {
                val plant = response.body()?.entity
                if (plant != null) {
                    Result.success(plant)
                } else {
                    Result.failure(Exception("Plant data is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }





}
