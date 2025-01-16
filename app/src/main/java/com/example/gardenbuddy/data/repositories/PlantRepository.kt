package com.example.gardenbuddy.data.repositories

import android.graphics.Bitmap
import com.example.gardenbuddy.data.models.Plant
import kotlinx.serialization.json.Json


object PlantRepository {



    suspend fun loadPlant(PlantId : Long) : Result<Plant> {
        return try {

            // intanto è così, aspettando su come fare il BE
            val result = """
            {
                "plantId": "${PlantId}",
                "scientificName": "Leucanthemum vulgare",
                "species": "Leucanthemum vulgare",
                "family": "Asteraceae",
                "parent": "Leucanthemum",
                "kingdom": "Plantae"
            }
            """.trimIndent()

            val plant: Plant = Json.decodeFromString(result)
            //plant.plantId = PlantId

            Result.success(plant)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchPlant(photo : Bitmap){

    }

    suspend fun searchPlant(name : String){

    }

}

