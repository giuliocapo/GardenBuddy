package com.example.gardenbuddy.data.models

import android.graphics.Bitmap

data class GardenPlant(
    val photos : List<Bitmap>,
    val gardenId : Long,
    val plantId : Long
)