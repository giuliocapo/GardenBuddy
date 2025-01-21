package com.example.gardenbuddy.ui.screens.gardenscreen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.gardenplantscreen.GardenPlantCard
import com.example.gardenbuddy.ui.screens.gardenplantscreen.GardenPlantScreen
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

    // Carica i dati
    LaunchedEffect(gardenId) {
        gardenScreenViewModel.loadGarden(gardenId)
        gardenScreenViewModel.loadGardenPlants(gardenId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Garden Details
        garden?.let {
            GardenCardContent(garden = it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Plant Search Section
        PlantSearchSection(plantScreenViewModel = plantScreenViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // GardenPlant List
        gardenPlants?.let { plants ->
            GardenPlantScreen(gardenPlants = plants)
        }
    }
}


    @Composable
    fun GardenCardContent(garden: Garden) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(4.dp, shape = MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Garden Name: ${garden.name}", style = MaterialTheme.typography.titleLarge)
                Text(text = "Dimension: ${garden.dimension} sqm", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Location: (${garden.latitude}, ${garden.longitude})", style = MaterialTheme.typography.bodySmall)
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


