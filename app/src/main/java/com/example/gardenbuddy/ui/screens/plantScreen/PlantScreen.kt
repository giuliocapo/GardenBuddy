package com.example.gardenbuddy.ui.screens.plantScreen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.SharedUserViewModel


@Composable
fun PlantScreen (
    navController: NavController,
    sharedUserViewModel: SharedUserViewModel,
    plantScreenViewModel: PlantScreenViewModel = viewModel()
){
    val errorMessage by plantScreenViewModel.errorMessage.collectAsState()
    val isLoading by plantScreenViewModel.isLoading.collectAsState()
    val plantSearchSuccess by plantScreenViewModel.plantSearchSuccess.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by Name") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    if (searchQuery.isNotBlank()) {
                        plantScreenViewModel.searchPlant(searchQuery)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Camera Button
        Button(
            onClick = {
                // Mock implementation for opening the camera
                openCamera(context) { bitmap ->
                    plantScreenViewModel.searchPlant(bitmap)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Search by Photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results or Error Message
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            when {
                plantSearchSuccess != null -> {
                    Text(text = "Search Result:", style = MaterialTheme.typography.titleMedium)
                    PlantCardContent(plantSearchSuccess!!.first())
                }

                errorMessage.isNotBlank() -> {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }

                else -> {
                    Text(text = "No results found.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun PlantCardContent(plant: Plant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Plant Name: ${plant.scientificName}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Species: ${plant.species}", style = MaterialTheme.typography.bodySmall)
            Text(text = "ID: ${plant.plantId}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Parent: ${plant.parent}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Kingdom: ${plant.kingdom}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun openCamera(context: Context, onImageCaptured: (Bitmap) -> Unit) {
    // Simulating opening a camera, capturing an image, and returning it as a Bitmap
    val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Replace with real camera capture logic
    onImageCaptured(bitmap)
}
