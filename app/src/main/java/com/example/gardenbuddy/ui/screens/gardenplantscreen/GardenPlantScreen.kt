package com.example.gardenbuddy.ui.screens.gardenplantscreen

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold
        )
        if (gardenPlants.isEmpty()){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "No plants in garden", fontSize = 20.sp)
            }
        }else{
            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                items(gardenPlants) { (plant, photos) ->
                    GardenPlantCard(plant = plant, photos = photos,  gardenScreenViewModel, gardenId)
                }
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
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Column(){
                    Text(
                        text = plant.scientificName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = plant.species,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                Row(){
                    IconButton(
                        onClick = {
                            if (!showCamera) showCamera = true },

                        ) {
                        Icon(
                            Icons.Rounded.AddAPhoto,
                            tint = Color.Black,
                            contentDescription = "Add photo",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            gardenScreenViewModel.removePlant(
                                plant.plantId,
                                gardenId
                            )
                        },
                    ) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = "Delete plant",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }


            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                PhotosCard(photos = photos)
                Spacer(modifier = Modifier.width(4.dp))
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

@Preview
@Composable
fun GardenPlantCardPreview() {
    val plant = Plant(
        scientificName = "Plant name",
        species = "Plant species",
        family = "Plant family",
        parent = "Plant parent",
        kingdom = "Plant kingdom",
        plantId = 1
    )

    val photos = listOf("photo1.jpg", "photo2.jpg")
    GardenPlantCard(plant = plant, photos = photos, GardenScreenViewModel(), 1)

}

