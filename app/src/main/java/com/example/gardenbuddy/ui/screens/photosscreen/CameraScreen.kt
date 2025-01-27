package com.example.gardenbuddy.ui.screens.photosscreen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreenViewModel
import java.io.ByteArrayOutputStream


@Composable
fun CameraButton(
    gardenScreenViewModel: GardenScreenViewModel,
    gardenId : Long,
    plantId : Long,
    onDismiss: () -> Unit,
    onSavePhotoClick: (gardenId : Long, plantId : Long, photos : List<String>) -> Unit // New parameter to handle the save photo action

) {
    var showPhotoDialog by remember { mutableStateOf(false) }
    var capturedPhotoBase64 by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            capturedPhotoBase64 = "data:image/jpeg;base64,$base64String"
            showPhotoDialog = true
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, launch camera
            cameraLauncher.launch(null)
        } else {
            // Permission denied, dismiss
            onDismiss()
        }
    }


    // Launch camera immediately
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) -> {
                // Permission already granted, launch camera
                cameraLauncher.launch(null)
            }
            else -> {
                // Request permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Photo dialog
    if (showPhotoDialog && capturedPhotoBase64 != null) {
        Dialog(
            onDismissRequest = {
                showPhotoDialog = false
                capturedPhotoBase64 = null
                onDismiss()
            }
        ) {
            Surface(
                modifier = Modifier
                    .width(320.dp)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PhotosCard(photos = listOf(capturedPhotoBase64!!))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = {
                                showPhotoDialog = false
                                capturedPhotoBase64 = null
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                capturedPhotoBase64?.let { base64 ->
                                    onSavePhotoClick(gardenId,
                                        plantId,
                                        listOf(base64))
                                }

                                showPhotoDialog = false
                                capturedPhotoBase64 = null
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save Photo")
                        }
                    }
                }
            }
        }
    }
}