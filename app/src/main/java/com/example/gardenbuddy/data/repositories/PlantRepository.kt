package com.example.gardenbuddy.data.repositories

import android.graphics.Bitmap
import com.example.gardenbuddy.data.Dtos.requests.SearchPhotoRequest
import com.example.gardenbuddy.data.Dtos.responses.SearchNameResponse
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.utils.RetrofitClient
import com.example.gardenbuddy.utils.bitmapConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement


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

    suspend fun searchPlant(photo: String, latitude: Double, longitude: Double): Result<Plant> {
        val dto = SearchPhotoRequest(photo, latitude, longitude)
        return try {
            val response = RetrofitClient.plantApiService.searchPlantByPhoto(dto)
            if (response.isSuccessful) {
                val plants = response.body()
                if (!plants.isNullOrEmpty()) {
                    Result.success(plants.first()) // Return the first plant from the list
                } else {
                    Result.failure(Exception("No plants found"))
                }
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchPlant(name : String) : Result<List<Plant>>{
        return try {
            val response = RetrofitClient.plantApiService.searchPlantByName(name)
            if (response.isSuccessful) {
                val plants = response.body()
                if (!plants.isNullOrEmpty()) {
                    Result.success(plants)
                } else {
                    Result.failure(Exception("No plants found"))
                }
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}

