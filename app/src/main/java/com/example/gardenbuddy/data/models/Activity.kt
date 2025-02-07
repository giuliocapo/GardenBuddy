package com.example.gardenbuddy.data.models

import kotlinx.serialization.Serializable

@Serializable // Utile per la serializzazione JSON
data class Activity(
    val id: String?,
    val userId: String,
    val username: String,
    val minutes: Int,
    val steps: Int,
    val calories: Int,
    val timestamp: Long?
)
