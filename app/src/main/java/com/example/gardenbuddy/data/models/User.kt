package com.example.gardenbuddy.data.models

import java.util.Date

data class User(
    val userId: String,
    val name: String,
    val birthdate: Date,
    val email: String,
    val weight: Double
)