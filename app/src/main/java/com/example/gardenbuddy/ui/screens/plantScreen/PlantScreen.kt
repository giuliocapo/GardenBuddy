package com.example.gardenbuddy.ui.screens.plantScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreenViewModel
import com.example.gardenbuddy.ui.screens.photosscreen.CameraButton

@Composable
fun PlantSearchSection(plantScreenViewModel: PlantScreenViewModel = viewModel(), gardenId: Long, gardenScreenViewModel: GardenScreenViewModel) {

    var searchQuery by remember { mutableStateOf("") }
    val plantSearchSuccess by plantScreenViewModel.plantSearchSuccess.collectAsState()
    val isLoading by plantScreenViewModel.isLoading.collectAsState()
    val isLoadingCoordinates by gardenScreenViewModel.isLoadingCoordinates.collectAsState()
    val errorMessage by plantScreenViewModel.errorMessage.collectAsState()
    var showCamera by remember { mutableStateOf(false) }
    val gardenCoordinates by gardenScreenViewModel.gardencoordinatesSuccess.observeAsState()

    LaunchedEffect(gardenId) {
        gardenScreenViewModel.getGardenCoordinates(gardenId)
    }

    Column {
        if (isLoadingCoordinates) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Row(modifier = Modifier.padding(8.dp)){
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search plants") },
                    trailingIcon = {
                        Row {
                            if (searchQuery.isBlank() && (gardenCoordinates?.first
                                    ?: 0.0) != 0.0 && (gardenCoordinates?.second ?: 0.0) != 0.0
                            ) {
                                IconButton(onClick = {
                                    if (!showCamera) showCamera = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Camera"
                                    )
                                }
                            }
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = {
                                    plantScreenViewModel.searchPlant(searchQuery)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage)
            }
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                plantSearchSuccess?.let { plants ->
                    LazyColumn {
                        items(plants) { plant ->
                            PlantCardContent(
                                plant = plant,
                                gardenScreenViewModel = gardenScreenViewModel,
                                gardenId = gardenId
                            )
                        }
                    }
                }
            }
        }
    }
    if (showCamera) {
        CameraButton(
            gardenId,
            0L, // unused param
            onDismiss = { showCamera = false },
            onSavePhotoClick = { garden_Id, plant_Id, _photos ->
                gardenCoordinates?.let { plantScreenViewModel.searchPlant(_photos.first(), it.first, it.second) }
            }
            )
    }
}

@Composable
fun PlantCardContent(
    plant: Plant,
    gardenScreenViewModel: GardenScreenViewModel,
    gardenId: Long
) {

    val gardenPlants by gardenScreenViewModel.gardenplantsSuccess.observeAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            .heightIn(max = 250.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Plant Name: ${plant.scientificName}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Species: ${plant.species}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Family: ${plant.family}", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Kingdom: ${plant.kingdom}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Parent: ${plant.parent}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    gardenScreenViewModel.addPlant(plant.plantId, gardenId, emptyList())
                },
                enabled = gardenPlants?.none { it.first.plantId == plant.plantId } ?: true,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f)
            ) {
                Text("Add")
            }
        }
    }
}
