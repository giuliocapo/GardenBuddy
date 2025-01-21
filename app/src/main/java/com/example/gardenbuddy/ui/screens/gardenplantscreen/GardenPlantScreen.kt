package com.example.gardenbuddy.ui.screens.gardenplantscreen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreenViewModel


@Composable
fun GardenPlantScreen(
    gardenPlants: List<Pair<Plant, List<Bitmap>>>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Plants in Garden",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(gardenPlants) { (plant, photos) ->
                GardenPlantCard(plant = plant, photos = photos)
            }
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
            Text(
                text = "Plant Name: ${plant.scientificName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Species: ${plant.species}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                items(photos) { photo ->
                    Image(
                        bitmap = photo.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 8.dp)
                            .clip(MaterialTheme.shapes.small)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }
            }
        }
    }
}
