package com.example.gardenbuddy.data.repositories

import android.graphics.Bitmap
import com.example.gardenbuddy.data.Dtos.responses.SearchNameResponse
import com.example.gardenbuddy.data.models.Plant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import okhttp3.OkHttpClient
import okhttp3.Request

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

    suspend fun searchPlant(photo : Bitmap) : Result<List<Plant>>{
        //TODO chiamata api

        return try {

            // intanto è così, aspettando su come fare il BE
            val result = """
            {
                "plantId": "${1234}",
                "scientificName": "Leucanthemum vulgare",
                "species": "Leucanthemum vulgare",
                "family": "Asteraceae",
                "parent": "Leucanthemum",
                "kingdom": "Plantae"
            }
            """.trimIndent()
            val plant: Plant = Json.decodeFromString(result)
            //plant.plantId = PlantId
            val plants = listOf(plant)

            Result.success(plants)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun searchPlant(name : String) : Result<List<Plant>>{
        //TODO chiamata api

        return Result.success(emptyList())
        /*val url = "https://api.gbif.org/v1/species/search?q=${name}&limit=1"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()
        var plant : Plant

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                println("Response code: ${response.code}")
                println("Response message: ${response.message}")
                println("Response body: ${response.body?.string()}")
                if (response.body == null) {
                    println("The response body is empty")
                }
                response.body?.string()?.let { jsonResponse ->
                    // Deserializza il JSON
                    val apiResponse = Json.decodeFromString<SearchNameResponse>(jsonResponse)
                    // Ottieni il valore di nameKey dal primo elemento
                    println(jsonResponse)

                    plant = apiResponse.results.firstOrNull() ?: return Result.failure(Exception("No plant found"))
                    println(plant)

                    val plants = listOf(plant)
                    println(plants)
                    return Result.success(plants)
                }
                println("response body is null")

                return Result.failure(Exception("response body is null"))
            }  else {
                println("response unsuccessful")

                return Result.failure(Exception("response unsuccessful"))

            }
        } catch (e: Exception) {
            println(e.message)

            return Result.failure(e)
        }*/

    }

}

