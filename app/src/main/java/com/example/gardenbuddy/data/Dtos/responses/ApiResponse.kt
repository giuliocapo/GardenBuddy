package com.example.gardenbuddy.data.Dtos.responses

data class ApiResponse<T>(
    val message: String,
    val entity: T
)
