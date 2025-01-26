package com.example.gardenbuddy.utils

import com.example.gardenbuddy.data.ApiInterfaces.GardenApiService
import com.example.gardenbuddy.data.ApiInterfaces.GardenPlantApiService
import com.example.gardenbuddy.data.ApiInterfaces.PlantApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://lawrenzo.pythonanywhere.com"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val gardenApiService: GardenApiService by lazy {
        retrofit.create(GardenApiService::class.java)
    }

    val plantApiService: PlantApiService by lazy {
        retrofit.create(PlantApiService::class.java)
    }

    val gardenPlantApiService: GardenPlantApiService by lazy {
        retrofit.create(GardenPlantApiService::class.java)
    }
}