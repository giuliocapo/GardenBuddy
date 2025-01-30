package com.example.gardenbuddy.ui.screens.gardenplantscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.gardenbuddy.data.models.Plant
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreenViewModel
import com.example.gardenbuddy.ui.screens.photosscreen.CameraButton
import com.example.gardenbuddy.ui.screens.photosscreen.PhotosCard


@Composable
fun GardenPlantScreen(
    gardenPlants: List<Pair<Plant, List<String>>>,
    gardenScreenViewModel : GardenScreenViewModel,
    gardenId : Long
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Plants in Garden",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
            items(gardenPlants) { (plant, photos) ->
                GardenPlantCard(plant = plant, photos = photos,  gardenScreenViewModel, gardenId)
            }
        }
    }
}

@Composable
fun GardenPlantCard(plant: Plant, photos: List<String>, gardenScreenViewModel: GardenScreenViewModel, gardenId : Long) {
    var showCamera by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = plant.scientificName,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Species: ${plant.species}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                PhotosCard(photos = photos)
                Spacer(modifier = Modifier.width(4.dp))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween // Ensure spacing between buttons
                ) {
                    Button(
                        onClick = {
                            if (!showCamera) showCamera = true },

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Photo")
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Space between the two buttons

                    Button(
                        onClick = {
                            gardenScreenViewModel.removePlant(
                                plant.plantId,
                                gardenId
                            )
                        },
                        modifier = Modifier.fillMaxWidth() // Match width with "Add Photo" button
                    ) {
                        Text("Remove Plant")
                    }
                }
            }
        }
    }
    if (showCamera) {
        CameraButton(
            gardenId,
            plant.plantId,
            onDismiss = { showCamera = false },
            onSavePhotoClick = { garden_Id, plant_Id, _photos ->
                gardenScreenViewModel.updateGardenPlant(garden_Id, plant_Id, _photos)
            }
        )
    }

}