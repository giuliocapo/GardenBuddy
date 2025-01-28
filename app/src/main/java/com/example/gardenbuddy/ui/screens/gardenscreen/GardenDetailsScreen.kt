package com.example.gardenbuddy.ui.screens.gardenscreen

import android.graphics.Bitmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.gardenplantscreen.GardenPlantCard
import com.example.gardenbuddy.ui.screens.gardenplantscreen.GardenPlantScreen
import com.example.gardenbuddy.ui.screens.homescreen.BottomNavigationBar
import com.example.gardenbuddy.ui.screens.photosscreen.PhotosCard
import com.example.gardenbuddy.ui.screens.plantScreen.PlantScreenViewModel
import com.example.gardenbuddy.ui.screens.plantScreen.PlantSearchSection


@Composable
fun GardenDetailsScreen(
    navController: NavHostController,
    gardenId: Long,
    sharedUserViewModel: SharedUserViewModel,
    gardenScreenViewModel: GardenScreenViewModel = viewModel(),
    plantScreenViewModel: PlantScreenViewModel = viewModel()
) {
    val gardenPlants by gardenScreenViewModel.gardenplantsSuccess.observeAsState(emptyList())
    val gardenPlantsIsLoading by gardenScreenViewModel.gardenPlantsisLoading.collectAsState()
    val selectedTab = remember { mutableStateOf("garden") }



    // Carica i dati
    LaunchedEffect(gardenId) {
        gardenScreenViewModel.loadGardenPlants(gardenId)
    }

    Scaffold(
    bottomBar = {
        BottomNavigationBar(
            navController = navController,
            selectedTab = selectedTab.value
        ) { tab ->
            selectedTab.value = tab
            navController.navigate(tab) // Cambia schermata al cambio di tab
        }
    },
    content = { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
                //Plant Search Section
                PlantSearchSection(plantScreenViewModel = plantScreenViewModel, gardenId, gardenScreenViewModel)

                Spacer(modifier = Modifier.height(8.dp))

                if (gardenPlantsIsLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    gardenPlants?.let { plants ->
                        GardenPlantScreen(
                            gardenPlants = plants,
                            gardenScreenViewModel = gardenScreenViewModel,
                            gardenId
                        )
                    }
                }

        }
    })


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


