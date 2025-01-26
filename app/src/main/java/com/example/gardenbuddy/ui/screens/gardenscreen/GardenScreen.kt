package com.example.gardenbuddy.ui.screens.gardenscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun GardenScreen(
    navController: NavController,
    sharedUserViewModel: SharedUserViewModel,
    gardenScreenViewModel: GardenScreenViewModel = viewModel()
) {
    val garden by gardenScreenViewModel.gardenLoadSuccess.collectAsState() // TODO verify this
    val isLoading by gardenScreenViewModel.isLoading.collectAsState()


    // Carica i giardini all'avvio
    LaunchedEffect(Unit) {
        // TODO user.userId get the user id
        gardenScreenViewModel.loadGarden(sharedUserViewModel.user.value!!.userId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Your Gardens",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        else if (garden == null) {
            CreateGarden(gardenScreenViewModel, sharedUserViewModel)
        }
        else {
            garden?.let {
                GardenCard(garden = it) {
                    navController.navigate("garden_details/${garden!!.id}")
                }
            }
        }
    }
}

@Composable
fun GardenCard(garden: Garden, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Garden Name: ${garden.name}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Dimension: ${garden.dimension} sqm", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun CreateGarden(
    gardenScreenViewModel: GardenScreenViewModel = viewModel(),
    sharedUserViewModel: SharedUserViewModel = viewModel()
) {

    // State variables for the form fields
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var latitude by remember { mutableStateOf(TextFieldValue("")) }
    var longitude by remember { mutableStateOf(TextFieldValue("")) }
    var dimension by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Create a New Garden",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Garden Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dimension,
            onValueChange = { dimension = it },
            label = { Text("Dimension (sq. meters)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Parse and validate input fields
                val gardenName = name.text.trim()
                val gardenLatitude = latitude.text.toDoubleOrNull()
                val gardenLongitude = longitude.text.toDoubleOrNull()
                val gardenDimension = dimension.text.toDoubleOrNull()
                println(gardenName)
                println(gardenLatitude)
                println(gardenLongitude)
                println(gardenDimension)

                if (gardenName.isNotBlank() && gardenLatitude != null && gardenLongitude != null && gardenDimension != null) {
                    val newGarden = Garden(
                        name = gardenName,
                        latitude = gardenLatitude,
                        longitude = gardenLongitude,
                        dimension = gardenDimension,
                        user_id = sharedUserViewModel.user.value!!.userId
                    )
                    // Call the ViewModel's function to handle garden creation
                    gardenScreenViewModel.saveGarden(newGarden)
                } else {
                    println("input non valid")
                    // Optionally show an error message if inputs are invalid
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create")
        }
    }
}
