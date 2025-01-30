package com.example.gardenbuddy.data.Dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotoRequest(
    val photo : String,
    val latitude : Double,
    val longitude : Double,
    val similar_images : Boolean = true
)