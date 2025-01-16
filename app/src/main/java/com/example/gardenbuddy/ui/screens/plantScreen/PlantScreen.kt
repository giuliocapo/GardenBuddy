package com.example.gardenbuddy.ui.screens.plantScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.data.models.Plant


@Composable
fun PlantScreen (
    navController: NavController,
    plantScreenViewModel: PlantScreenViewModel = viewModel()
){
    val errorMessage by plantScreenViewModel.errorMessage.collectAsState()
    val isLoading by plantScreenViewModel.isLoading.collectAsState()
    val plantLoadSuccess by plantScreenViewModel.plantLoadSuccess.collectAsState()
    LaunchedEffect(Unit) {
        plantScreenViewModel.loadPlant(1234)
    }

    if (isLoading) {
        // Show loading indicator
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else {
        if (plantLoadSuccess != null) {
            // Continue with the plant load success UI
            Text(text = "Plant loaded successfully!")
            PlantCardContent(plantLoadSuccess!!)
        } else {
            // Display an error message from `errorMessage`
            Text(text = errorMessage ?: "An unknown error occurred.")
        }
    }
}

@Composable
fun PlantCardContent(plant: Plant) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Plant Name: ${plant.scientificName}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Species: ${plant.species}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Id: ${plant.plantId}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Parent: ${plant.parent}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Kingdom: ${plant.kingdom}", style = MaterialTheme.typography.bodySmall)

    }
}