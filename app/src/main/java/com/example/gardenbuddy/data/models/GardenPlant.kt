package com.example.gardenbuddy.data.models

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

data class GardenPlant(
    val photos : List<String>,
    val gardenId : Long,
    val plantId : Long
)

fun convertBitmapsToByteArrays(bitmaps: List<Bitmap>): List<ByteArray> {
    return bitmaps.map { bitmap ->
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        byteArrayOutputStream.toByteArray()
    }}