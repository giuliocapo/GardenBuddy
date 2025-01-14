package com.example.gardenbuddy.data.models

import java.util.Date


data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val weight: Double = 0.0,
    val birthdate: Date? = null
)
