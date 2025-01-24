package com.example.gardenbuddy.ui.screens.gardenscreen

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment

@Composable
fun GardenScreen(
    navController: NavController,
    sharedUserViewModel: SharedUserViewModel,
    gardenScreenViewModel: GardenScreenViewModel = viewModel()
) {
    val garden by gardenScreenViewModel.gardenLoadSuccess.collectAsState() // TODO verify this
    val isLoading by gardenScreenViewModel.isLoading.collectAsState()
    val user by sharedUserViewModel.user.collectAsState()


    // Carica i giardini all'avvio
    LaunchedEffect(Unit) {
        // TODO user.userId get the user id
        gardenScreenViewModel.loadGarden(1L)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Your Gardens",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
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
