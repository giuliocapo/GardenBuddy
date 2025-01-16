package com.example.gardenbuddy.data.models

import android.graphics.Bitmap

data class Garden(
    val id : Long,
    val name : String,
    val latitude : Double,
    val longitude : Double,
    val dimension : Double,
    val photos : List<Bitmap>
)