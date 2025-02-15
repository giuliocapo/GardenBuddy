package com.example.gardenbuddy.ui.screens.photosscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.max


@Composable
fun PhotosCard(photos: List<String>) {

    var currentPhotoIndex by remember { mutableStateOf(0) }
    if (photos.isNotEmpty()) {
        val imageWidth = 250.dp
        val aspectRatio = 16f / 9f
        val imageHeight = 200.dp
        Card(
            modifier = Modifier
                .size(imageWidth, imageHeight)
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Display the current photo
                val base64Photo = photos[currentPhotoIndex]
                val cleanBase64 = base64Photo.replace("data:image/jpeg;base64,", "")

                val decodedByteArray = Base64.decode(cleanBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)

                Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
                //DrawImageOnCanvas(bitmap = bitmap, modifier = Modifier.fillMaxSize())

                // Navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(photos.size > 1) {
                        // Left button for the previous photo
                        IconButton(
                            onClick = {
                                currentPhotoIndex = if (currentPhotoIndex > 0) {
                                    currentPhotoIndex - 1
                                } else {
                                    photos.lastIndex
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous Photo",
                                tint = Color.Black
                            )
                        }
                        // Right button for the next photo
                        IconButton(
                            onClick = {
                                currentPhotoIndex = if (currentPhotoIndex < photos.lastIndex) {
                                    currentPhotoIndex + 1
                                } else {
                                    0
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Photo",
                                tint = Color.Black,

                            )
                        }
                    }
                }
            }
        }
    } else {
        Text("No photo", modifier = Modifier.padding(8.dp))
    }
}


// Function that handles the canvas drawing logic
@Composable
fun DrawImageOnCanvas(
    bitmap: Bitmap,
    modifier: Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val imageWidth = bitmap.width.toFloat()
        val imageHeight = bitmap.height.toFloat()

        val scaleFactor = max(canvasWidth / imageWidth, canvasHeight / imageHeight)
        val scaledWidth = imageWidth * scaleFactor
        val scaledHeight = imageHeight * scaleFactor

        // Calcola gli offset per centrare l'immagine
        val offsetX = (canvasWidth - scaledWidth) / 2
        val offsetY = (canvasHeight - scaledHeight) / 2

        // Disegna l'immagine scalata sul Canvas
        with(drawContext.canvas) {
            save()
            translate(offsetX, offsetY)
            scale(scaleFactor, scaleFactor)
            drawImage(bitmap.asImageBitmap())
            restore()
        }
    }
}
