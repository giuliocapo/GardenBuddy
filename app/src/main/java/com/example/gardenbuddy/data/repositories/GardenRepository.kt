package com.example.gardenbuddy.data.repositories

import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.utils.RetrofitClient



object GardenRepository {

    suspend fun loadGarden(userId: String): Result<Garden> {
        return try {
            val response = RetrofitClient.gardenApiService.getGardenById(userId)
            if (response.isSuccessful) {
                val garden = response.body()
                if (garden != null) {
                    Result.success(garden)
                } else {
                    Result.failure(Exception("Garden data is null"))
                }
            } else {
                Result.failure(Exception(Exception("Error: ${response.body() ?: ""}")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadGardenByGardenId(gardenId: Long): Result<Garden> {
        return try {
            val response = RetrofitClient.gardenApiService.getGardenByGardenId(gardenId)
            if (response.isSuccessful) {
                val garden = response.body()
                if (garden != null) {
                    Result.success(garden)
                } else {
                    Result.failure(Exception("Garden data is null"))
                }
            } else {
                Result.failure(Exception(Exception("Error: ${response.body() ?: ""}")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun saveGarden(garden : Garden) : Result<Garden> {
        return try {
            val response = RetrofitClient.gardenApiService.createGarden(garden)
            if (response.isSuccessful) {
                val garden = response.body()?.entity
                if (garden != null) {
                    Result.success(garden)
                } else {
                    Result.failure(Exception("Garden data is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateGarden(gardenId : Long, garden : Garden) : Result<Garden> {
        return try {
            val response = RetrofitClient.gardenApiService.updateGarden(gardenId, garden)
            if (response.isSuccessful) {
                val garden = response.body()?.entity
                if (garden != null) {
                    Result.success(garden)
                } else {
                    Result.failure(Exception("Garden data is null"))
                }
            } else {
                Result.failure(Exception("Error: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun deleteGarden(gardenId : Long) : Result<String> {
        return try {
            val response = RetrofitClient.gardenApiService.deleteGarden(gardenId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "")
            } else {
                Result.failure(Exception("Error: ${response.body() ?: ""}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}