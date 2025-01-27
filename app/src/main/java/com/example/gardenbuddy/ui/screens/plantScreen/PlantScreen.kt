package com.example.gardenbuddy.ui.screens.plantScreen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.gardenbuddy.data.models.GardenPlant
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreenViewModel

@Composable
fun PlantSearchSection(plantScreenViewModel: PlantScreenViewModel = viewModel(), gardenId: Long, gardenScreenViewModel: GardenScreenViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val plantSearchSuccess by plantScreenViewModel.plantSearchSuccess.collectAsState()
    val isLoading by plantScreenViewModel.isLoading.collectAsState()
    val errorMessage by plantScreenViewModel.errorMessage.collectAsState()
    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by Name") },
            trailingIcon = {
                IconButton(onClick = {
                    plantScreenViewModel.clearSearchResults()
                    if (searchQuery.isNotBlank()) {
                        plantScreenViewModel.searchPlant(searchQuery)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            plantSearchSuccess?.let { plants ->
                // Iterate over each plant in the list and display the PlantCardContent
                LazyColumn { // Using LazyColumn for a more efficient rendering of lists
                    items(plants) { plant ->
                        PlantCardContent(plant = plant, gardenScreenViewModel = gardenScreenViewModel, gardenId = gardenId)
                    }
                }
            }
        }
    }
}

@Composable
fun PlantCardContent(
    plant: Plant,
    gardenScreenViewModel: GardenScreenViewModel,
    gardenId: Long
) {
    //val gardenPlants by gardenScreenViewModel.gardenplantsLoadSuccess.collectAsState()
    val gardenPlants by gardenScreenViewModel.gardenplantsSuccess.observeAsState()


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            .heightIn(max = 250.dp), // Adjusting the card height
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Plant Name at the top center
            Text(
                text = "Plant Name: ${plant.scientificName}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Two-column layout for the other text fields
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

            // Add button at the bottom center
            Button(
                onClick = {
                    gardenScreenViewModel.addPlant(plant.plantId, gardenId, emptyList())
                },
                enabled = gardenPlants?.none { it.first.plantId == plant.plantId } ?: true,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f) // Make the button smaller than full width
            ) {
                Text("Add")
            }
        }
    }
}
