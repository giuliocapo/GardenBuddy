package com.example.gardenbuddy.ui.screens.photosscreen

import android.util.Base64
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun PhotosCard(photos: List<String>) {

    var currentPhotoIndex by remember { mutableStateOf(0) }
    if (photos.isNotEmpty()) {
        Card(
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Display the current photo
                val base64Photo = photos[currentPhotoIndex]
                val cleanBase64 = base64Photo.replace("data:image/jpeg;base64,", "")
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Base64.decode(cleanBase64, Base64.DEFAULT))
                        .build(),
                    contentDescription = "Photo $currentPhotoIndex",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous Photo",
                                tint = Color.White
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
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next Photo",
                                tint = Color.White
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
