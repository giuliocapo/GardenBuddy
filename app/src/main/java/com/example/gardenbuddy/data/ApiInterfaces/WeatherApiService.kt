package com.example.gardenbuddy.data.ApiInterfaces

import com.example.gardenbuddy.data.models.WeatherInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(@Query("lat") lat: String, @Query("long") long: String): Response<WeatherInfo>
}