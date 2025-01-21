package com.example.gardenbuddy.data.models

import android.graphics.Bitmap
import kotlinx.serialization.Serializable

@Serializable
data class Garden(
    val id : Long,
    val name : String,
    val latitude : Double,
    val longitude : Double,
    val dimension : Double,
    val photos: List<ByteArray>,
    val user_id : Long

)