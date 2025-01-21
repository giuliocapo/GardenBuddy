package com.example.gardenbuddy.ui.screens.plantScreen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
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
fun PlantSearchSection(plantScreenViewModel: PlantScreenViewModel) {
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
            plantSearchSuccess?.let { plant ->
                PlantCardContent(plant = plant.first())
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
            Text(text = "Plant Name: ${plant.scientificName}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Species: ${plant.species}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Family: ${plant.family}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
