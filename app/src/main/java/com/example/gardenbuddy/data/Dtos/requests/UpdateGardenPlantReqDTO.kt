package com.example.gardenbuddy.data.Dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateGardenPlantRequest(
    val photos: List<String>
)