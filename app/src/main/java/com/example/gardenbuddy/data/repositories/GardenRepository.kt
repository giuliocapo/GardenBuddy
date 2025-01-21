package com.example.gardenbuddy.data.repositories

import android.graphics.Bitmap
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.data.models.GardenPlant
import com.example.gardenbuddy.data.models.Plant
import kotlinx.serialization.json.Json

object GardenRepository {

    suspend fun loadGarden(gardenId : Long) : Result<Garden> {
        return try {
            // intanto è così, aspettando su come fare il BE
            val result = """
            {
                "id": "${1234}",
                "name": "eden",
                "latitude": 123.123,
                "longitude": 456.456,
                "dimension": 100,
                "photos": [],
                "user_id": 1
            }
            """.trimIndent()

            val garden: Garden = Json.decodeFromString(result)

            Result.success(garden)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveGarden(garden : Garden) : Result<Garden> {
        // TODO implement
        return Result.success(garden)
    }


    suspend fun deleteGarden(gardenId : Long) : Result<String> {
        // TODO implement delete also the entries of GardenPlantRepository of that Garden Id
        return Result.success("Garden deleted")
    }



    suspend fun getAllGardens(userId : Long) : Result<List<Garden>> {
        // TODO implement
        val gardens = listOf(GardenRepository.loadGarden(1234).getOrThrow())
        if(gardens.isEmpty()) return Result.failure(Exception("No gardens found"))
        return Result.success(gardens)
    }

}