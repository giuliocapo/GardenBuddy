package com.example.gardenbuddy.data.repositories

import com.example.gardenbuddy.data.Dtos.requests.SearchPhotoRequest
import com.example.gardenbuddy.data.Dtos.responses.ResponseDto
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.utils.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



object PlantRepository {

    suspend fun loadPlant(plantId : Long) : Result<Plant> {
        return try {
            val response = RetrofitClient.plantApiService.getPlantById(plantId)
            if (response.isSuccessful) {
                val plant = response.body()
                if (plant != null) {
                    Result.success(plant)
                } else {
                    Result.failure(Exception("Garden data is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchPlant(photo: String, latitude: Double, longitude: Double): Result<List<Plant>> {
        val dto = SearchPhotoRequest(photo, latitude, longitude)
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.plantApiService.searchPlantByPhoto(dto)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                        ?: return@withContext Result.failure(Exception("Empty response body"))

                    when (responseBody) {
                        is ResponseDto.Success -> {
                            val plants = responseBody.results.results
                            if (plants.isNotEmpty()) {
                                return@withContext Result.success(plants) // Return the first plant from the list
                            } else {
                                return@withContext Result.failure(Exception("No plants found"))
                            }
                        }
                        is ResponseDto.Error -> {

                            return@withContext Result.failure(Exception(responseBody.errorResponse.error))
                        }
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    return@withContext Result.failure(Exception("Error: $errorMessage"))
                }
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    suspend fun searchPlant(name : String) : Result<List<Plant>>{
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.plantApiService.searchPlantByName(name)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                        ?: return@withContext Result.failure(Exception("Empty response body"))

                    when (responseBody) {
                        is ResponseDto.Success -> {
                            val plants = responseBody.results.results
                            if (plants.isNotEmpty()) {
                                return@withContext Result.success(plants)
                            } else {
                                return@withContext Result.failure(Exception("No plants found"))
                            }
                        }
                        is ResponseDto.Error -> {
                            return@withContext Result.failure(Exception(responseBody.errorResponse.error))
                        }
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    return@withContext Result.failure(Exception("Error: $errorMessage"))
                }
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }

    }

}

