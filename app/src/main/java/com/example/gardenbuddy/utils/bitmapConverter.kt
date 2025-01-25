package com.example.gardenbuddy.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object bitmapConverter {

    fun bitmapToBase64String(bitmap: Bitmap): String {
        // Convert the Bitmap to a ByteArrayOutputStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        // Compress the Bitmap into JPEG format and write to the stream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        // Get the byte array from the stream
        val byteArray = byteArrayOutputStream.toByteArray()
        // Encode the byte array to Base64
        val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        // Return the string in the desired format
        return "data:image/jpeg;base64,$base64String"
    }

    fun base64StringToBitmap(base64String: String): Bitmap {
        // Remove the data URI prefix if present
        val base64Image = base64String.substringAfter(",")

        // Decode the Base64 string to byte array
        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)

        // Convert byte array to Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}