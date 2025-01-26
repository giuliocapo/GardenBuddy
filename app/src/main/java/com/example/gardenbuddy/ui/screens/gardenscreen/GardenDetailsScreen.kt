package com.example.gardenbuddy.ui.screens.gardenscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.gardenplantscreen.GardenPlantCard
import com.example.gardenbuddy.ui.screens.gardenplantscreen.GardenPlantScreen
import com.example.gardenbuddy.ui.screens.photosscreen.PhotosCard
import com.example.gardenbuddy.ui.screens.plantScreen.PlantScreenViewModel
import com.example.gardenbuddy.ui.screens.plantScreen.PlantSearchSection

@Composable
fun GardenDetailsScreen(
    navController: NavController,
    gardenId: Long,
    sharedUserViewModel: SharedUserViewModel,
    gardenScreenViewModel: GardenScreenViewModel = viewModel(),
    plantScreenViewModel: PlantScreenViewModel = viewModel()
) {
    val garden by gardenScreenViewModel.gardenLoadSuccess.collectAsState()
    val gardenPlants by gardenScreenViewModel.gardenplantsLoadSuccess.collectAsState()
    val isLoading by gardenScreenViewModel.isLoading.collectAsState()
    val gardenPlantsIsLoading by gardenScreenViewModel.gardenPlantsisLoading.collectAsState()



    // Carica i dati
    LaunchedEffect(gardenId) {
        gardenScreenViewModel.loadGardenByGardenId(gardenId)
        gardenScreenViewModel.loadGardenPlants(gardenId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Garden Details
            garden?.let {
                GardenCardContent(garden = garden!!, gardenScreenViewModel = gardenScreenViewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Plant Search Section
            PlantSearchSection(plantScreenViewModel = plantScreenViewModel, gardenId, gardenScreenViewModel)

            Spacer(modifier = Modifier.height(16.dp))

            if (gardenPlantsIsLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                gardenPlants?.let { plants ->
                    GardenPlantScreen(gardenPlants = plants)
                }
            }
        }
    }
}


    @Composable
    fun GardenCardContent(garden: Garden, gardenScreenViewModel: GardenScreenViewModel) {
        var isEditing by remember { mutableStateOf(false) }
        var editedName by remember { mutableStateOf(garden.name) }
        var editedDimension by remember { mutableStateOf(garden.dimension) }
        var editedLatitude by remember { mutableStateOf(garden.latitude) }
        var editedLongitude by remember { mutableStateOf(garden.longitude) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(4.dp, shape = MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (isEditing) {
                    // Editing mode
                    TextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Garden Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = editedDimension.toString(),
                        onValueChange = { editedDimension = it.toDoubleOrNull() ?: garden.dimension },
                        label = { Text("Dimension (sqm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = editedLatitude.toString(),
                        onValueChange = { editedLatitude = it.toDoubleOrNull() ?: garden.latitude },
                        label = { Text("Latitude") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = editedLongitude.toString(),
                        onValueChange = { editedLongitude = it.toDoubleOrNull() ?: garden.longitude },
                        label = { Text("Longitude") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row {
                        Button(onClick = {
                            gardenScreenViewModel.updateGarden(garden.id, garden.copy(
                                name = editedName,
                                dimension = editedDimension,
                                latitude = editedLatitude,
                                longitude = editedLongitude
                            ))
                            isEditing = false
                        }) {
                            Text("Save")
                        }
                        Button(onClick = { isEditing = false }) {
                            Text("Cancel")
                        }
                    }
                } else {
                    // View mode
                    Text(text = "Garden Name: ${garden.name}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Dimension: ${garden.dimension} sqm", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Location: (${garden.latitude}, ${garden.longitude})", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Id: ${garden.id}", style = MaterialTheme.typography.bodyMedium)
                    PhotosCard(photos = garden.photos)

                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
                    }
                }
            }
        }
    }

    @Composable
    fun GardenPlantList(gardenPlants: List<Pair<Plant, List<Bitmap>>>) {
        LazyColumn {
            items(gardenPlants) { (plant, photos) ->
                GardenPlantCard(plant = plant, photos = photos)
            }
        }
    }

    @Composable
    fun GardenPlantCard(plant: Plant, photos: List<Bitmap>) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(4.dp, shape = MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Plant Name: ${plant.scientificName}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Species: ${plant.species}", style = MaterialTheme.typography.bodySmall)
                LazyRow {
                    items(photos) { photo ->
                        Image(
                            bitmap = photo.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }


