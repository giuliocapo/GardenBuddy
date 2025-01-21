package com.example.gardenbuddy.data.models

data class Activity(
    val id: String,
    val userId: String,
    val username: String,
    val minutes: Int,
    val steps: Int,
    val calories: Int,
    val timestamp: Long
)
