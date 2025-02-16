package com.example.gardenbuddy.ui.screens.gardenscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.gardenbuddy.data.models.Garden
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.gardenbuddy.ui.screens.homescreen.BottomNavigationBar
import com.example.gardenbuddy.ui.screens.photosscreen.CameraButton
import com.example.gardenbuddy.ui.screens.photosscreen.PhotosCard
import com.example.gardenbuddy.utils.LocationUtils
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun GardenScreen(
    navController: NavHostController,
    sharedUserViewModel: SharedUserViewModel,
    gardenScreenViewModel: GardenScreenViewModel = viewModel()
) {
    val garden by gardenScreenViewModel.gardenLoadSuccess.collectAsState()
    val isLoading by gardenScreenViewModel.isLoading.collectAsState()
    val selectedTab = remember { mutableStateOf("garden") }

    LaunchedEffect(Unit) {
        gardenScreenViewModel.loadGarden(sharedUserViewModel.user.value!!.userId)
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, sharedUserViewModel = sharedUserViewModel, selectedTab = selectedTab.value) {
                selectedTab.value = it
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
            {
                Text(
                    text = "Your Garden",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                if (isLoading) {
                    Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        CircularProgressIndicator()
                    }

                }
                else if (garden == null) {
                    CreateGarden(gardenScreenViewModel, sharedUserViewModel)
                }
                else {
                    garden?.let {
                        GardenCardContent(garden = it, gardenScreenViewModel) {
                            navController.navigate("garden_details/${garden!!.id}")
                        }
                    }
                }
            }
        })
    }


@Composable
fun GardenCardContent(
    garden: Garden,
    gardenScreenViewModel: GardenScreenViewModel,
    onNavigate: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(garden.name) }
    var editedDimension by remember { mutableStateOf(garden.dimension) }
    var editedLatitude by remember { mutableStateOf(garden.latitude) }
    var editedLongitude by remember { mutableStateOf(garden.longitude) }
    var showCamera by remember { mutableStateOf(false) }
    val hasLocationPermission by gardenScreenViewModel.hasLocationPermission.collectAsState()
    val currentLocation by gardenScreenViewModel.currentLocation.collectAsState()
    var isUpdatingLocation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val permissionLauncher = LocationUtils.rememberLocationPermissionLauncher {
        gardenScreenViewModel.updatePermissionStatus(it)
    }

    LaunchedEffect(Unit) {
        gardenScreenViewModel.checkInitialPermission(activity)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isEditing) {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Garden Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                )

                OutlinedTextField(
                    value = editedDimension.toString(),
                    onValueChange = { editedDimension = it.toDoubleOrNull() ?: editedDimension },
                    label = { Text("Dimension (sqm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editedLatitude.toString(),
                        onValueChange = { input -> editedLatitude = input.toDoubleOrNull() ?: editedLatitude },
                        label = { Text("Latitude") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = editedLongitude.toString(),
                        onValueChange = { input -> editedLongitude = input.toDoubleOrNull() ?: editedLongitude },
                        label = { Text("Longitude") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                if (hasLocationPermission) {
                    Button(onClick = {
                        isUpdatingLocation = true
                        gardenScreenViewModel.fetchCurrentLocation(activity) }) {
                        Text("Get Current Location")
                    }
                    if (isUpdatingLocation && currentLocation != null) {
                        currentLocation?.let { (latitude_input, longitude_input) ->
                            editedLatitude = latitude_input
                            editedLongitude = longitude_input
                            isUpdatingLocation = false // Reset after updating
                        }
                    }
                } else {
                    Button(onClick = {
                        gardenScreenViewModel.requestLocationPermission(permissionLauncher)
                    }) {
                        Text("Request Location Permission")
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        if (editedName.isNotBlank()) {
                            gardenScreenViewModel.updateGarden(
                                garden.id,
                                garden.copy(name = editedName, dimension = editedDimension, latitude = editedLatitude, longitude = editedLongitude, photos = emptyList())
                            )
                            isEditing = false
                        }
                    }) {
                        Text("Save")
                    }
                    OutlinedButton(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                Text(text = garden.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = "Dimension: ${garden.dimension} sqm", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Location: (${garden.latitude}, ${garden.longitude})", style = MaterialTheme.typography.bodySmall)

                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(garden.photos) { photo ->
                        val base64Data = photo.replace("data:image/jpeg;base64,", "")
                        val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

                        LaunchedEffect(photo) {
                            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                            imageBitmap.value = bitmap?.asImageBitmap()
                        }

                        imageBitmap.value?.let { imgBitmap ->
                            Image(



                                painter = BitmapPainter(imgBitmap),
                                contentDescription = "Photo",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }

                Column() {
                    OutlinedButton(onClick = { showCamera = true }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.CameraAlt,  contentDescription = "Add Photo")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Photo")
                    }

                    Button(onClick = { isEditing = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Edit Garden")
                    }

                    Button(onClick = onNavigate, modifier = Modifier.fillMaxWidth()) {
                        Text("Open Garden Details")
                    }
                }


            }
        }
    }

    if (showCamera) {
        CameraButton(
            garden.id,
            0L, // unused param
            onDismiss = { showCamera = false },
            onSavePhotoClick = { garden_Id, plant_Id, _photos ->
                gardenScreenViewModel.updateGarden(garden_Id, garden.copy(
                    name = garden.name,
                    dimension = garden.dimension,
                    latitude = garden.latitude,
                    longitude = garden.longitude,
                    photos = _photos
                ))
            }
        )
    }
}

@Composable
fun CreateGarden(
    gardenScreenViewModel: GardenScreenViewModel = viewModel(),
    sharedUserViewModel: SharedUserViewModel = viewModel()
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var latitude by remember { mutableStateOf(Double.NaN) }
    var longitude by remember { mutableStateOf(Double.NaN) }
    var dimension by remember { mutableStateOf(Double.NaN) }
    val hasLocationPermission by gardenScreenViewModel.hasLocationPermission.collectAsState()
    val currentLocation by gardenScreenViewModel.currentLocation.collectAsState()
    var isUpdatingLocation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val permissionLauncher = LocationUtils.rememberLocationPermissionLauncher { isGranted ->
        gardenScreenViewModel.updatePermissionStatus(isGranted)
    }

    LaunchedEffect(Unit) {
        gardenScreenViewModel.checkInitialPermission(activity)
    }

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

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Garden Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = if(latitude.isNaN().not()){ latitude.toString()} else {
                ""
            },
            onValueChange = { latitude = it.toDoubleOrNull() ?: latitude },
            label = { Text("Latitude") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = if(longitude.isNaN().not()){ longitude.toString()} else {
                ""
            },
            onValueChange = { longitude = it.toDoubleOrNull() ?: longitude },
            label = { Text("Longitude") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = if(dimension.isNaN().not()){ dimension.toString()} else {
                ""
            },
            onValueChange = { dimension = it.toDoubleOrNull() ?: dimension },
            label = { Text("Dimension (sq. meters)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        if (hasLocationPermission) {
            Button(onClick = {
                isUpdatingLocation = true
                gardenScreenViewModel.fetchCurrentLocation(activity) }) {
                Text("Get Current Location")
            }
            if (isUpdatingLocation && currentLocation != null) {
                currentLocation?.let { (latitude_input, longitude_input) ->
                    latitude = latitude_input
                    longitude = longitude_input
                    isUpdatingLocation = false // Reset after updating
                }
            }
        } else {
            Button(onClick = {
                gardenScreenViewModel.requestLocationPermission(permissionLauncher)
            }) {
                Text("Request Location Permission")
            }
        }
        Button(
            onClick = {
                val gardenName = name.text.trim()
                val gardenLatitude = latitude
                val gardenLongitude = longitude
                val gardenDimension = dimension

                if (gardenName.isNotBlank() && gardenLatitude.isNaN().not() && gardenLongitude.isNaN().not() && gardenDimension.isNaN().not()) {
                    val newGarden = Garden(
                        name = gardenName,
                        latitude = gardenLatitude,
                        longitude = gardenLongitude,
                        dimension = gardenDimension,
                        user_id = sharedUserViewModel.user.value!!.userId
                    )
                    gardenScreenViewModel.saveGarden(newGarden)
                } else {
                    Toast.makeText(context, "All the fields should be filled", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create")
        }
    }
}
