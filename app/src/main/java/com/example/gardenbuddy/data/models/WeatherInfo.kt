package com.example.gardenbuddy.data.models

data class WeatherInfo(
    val temperature: Double = 0.0,
    val humidity: Double = 0.0,
    val windSpeed: Double = 0.0,
    val description: String = "",
    val location: String = ""
)
