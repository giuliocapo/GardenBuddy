package com.example.gardenbuddy.data.models

import android.graphics.Bitmap
import kotlinx.serialization.Serializable

@Serializable
data class Garden(
    val id : Long = 0L,
    val name : String,
    val latitude : Double,
    val longitude : Double,
    val dimension : Double,
    val photos: List<String> = emptyList(),
    val user_id : String

)