package com.example.gardenbuddy.ui.screens.photosscreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.compose.ManagedActivityResultLauncher
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
import java.io.ByteArrayOutputStream
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


fun recognizeImage(context: Context, bitmap: Bitmap) {
    val image = InputImage.fromBitmap(bitmap, 0)

    // Configura il client con le opzioni predefinite
    val labelerOptions = ImageLabelerOptions.Builder()
        .setConfidenceThreshold(0.7f) // Soglia di confidenza per l'etichettatura (opzionale)
        .build()

    val labeler: ImageLabeler = ImageLabeling.getClient(labelerOptions)

    labeler.process(image)
        .addOnSuccessListener { labels ->
            // Stampa i label riconosciuti per il debugging
            labels.forEach { label ->
                println("MLKitDebug Label: ${label.text}, Confidence: ${label.confidence}")
            }

            // Controlla se l'immagine contiene etichette pertinenti a giardini o piante
            val validLabels = labels.any { label: ImageLabel ->
                label.text.contains("garden", ignoreCase = true) || label.text.contains("plant", ignoreCase = true) || label.text.contains("flower", ignoreCase = true)
            }
            if(!validLabels){
                Toast.makeText(context, "it seems there is no plant or garden in the image", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(context, "plant or garden detected", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "it seems there is no plant or garden in the image", Toast.LENGTH_SHORT).show()
        }
}

// Funzione per la gestione della fotocamera
fun launchCamera(
    cameraLauncher: ManagedActivityResultLauncher<Void?, Bitmap?>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    context: Context
) {
    val permissionStatus = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    )

    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        cameraLauncher.launch(null)
    } else {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

@Composable
fun CameraButton(
    gardenId: Long,
    plantId: Long,
    onDismiss: () -> Unit,
    onSavePhotoClick: (gardenId: Long, plantId: Long, photos: List<String>) -> Unit
) {
    var showPhotoDialog by remember { mutableStateOf(false) }
    var capturedPhotoBase64 by remember { mutableStateOf<String?>(null) }
    var isPhotoValid by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            capturedPhotoBase64 = "data:image/jpeg;base64,$base64String"
            showPhotoDialog = true
            // Riconoscimento dell'immagine
            recognizeImage(context, it)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        launchCamera(cameraLauncher, permissionLauncher, context)
    }

    // Dialogo per la foto
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
                                    onSavePhotoClick(gardenId, plantId, listOf(base64))
                                }
                                showPhotoDialog = false
                                capturedPhotoBase64 = null
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
    }
}
