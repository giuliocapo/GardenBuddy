package com.example.gardenbuddy.data.Dtos.responses

import com.example.gardenbuddy.data.models.Plant
import kotlinx.serialization.Serializable

@Serializable
data class SearchNameResponse(
    val count: Int,
    val results: List<Plant>
)