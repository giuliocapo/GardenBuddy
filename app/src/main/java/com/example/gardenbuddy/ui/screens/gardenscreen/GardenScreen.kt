package com.example.gardenbuddy.ui.screens.gardenscreen

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.gardenbuddy.ui.screens.homescreen.BottomNavigationBar
import com.example.gardenbuddy.ui.screens.photosscreen.CameraButton
import com.example.gardenbuddy.ui.screens.photosscreen.PhotosCard
import com.example.gardenbuddy.utils.LocationUtils
import com.example.gardenbuddy.utils.LocationUtils.getCurrentLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@Composable
fun GardenScreen(
    navController: NavHostController,
    sharedUserViewModel: SharedUserViewModel,
    gardenScreenViewModel: GardenScreenViewModel = viewModel()
) {
    val garden by gardenScreenViewModel.gardenLoadSuccess.collectAsState() // TODO verify this
    val isLoading by gardenScreenViewModel.isLoading.collectAsState()
    val selectedTab = remember { mutableStateOf("garden") }


    // Carica i giardini all'avvio
    LaunchedEffect(Unit) {
        // TODO user.userId get the user id
        gardenScreenViewModel.loadGarden(sharedUserViewModel.user.value!!.userId)
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedTab = selectedTab.value
            ) { tab ->
                selectedTab.value = tab
                navController.navigate(tab) // Cambia schermata al cambio di tab
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
                        GardenCardContent(garden = it, gardenScreenViewModel) {
                            navController.navigate("garden_details/${garden!!.id}")
                        }
                    }
                }

            }
        })
    }

@Composable
fun GardenCardContent(garden: Garden, gardenScreenViewModel: GardenScreenViewModel, onClick: () -> Unit) {
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

    // Handle permission result
    val permissionLauncher = LocationUtils.rememberLocationPermissionLauncher { isGranted ->
        gardenScreenViewModel.updatePermissionStatus(isGranted)
    }
    LaunchedEffect(Unit) {
        gardenScreenViewModel.checkInitialPermission(activity)
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                // Editing mode
                TextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Garden Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedDimension.toString(),
                    onValueChange = { editedDimension = it.toDoubleOrNull() ?: editedDimension },
                    label = { Text("Dimension (sqm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedLatitude.toString(),
                    onValueChange = { input ->
                        println("input: $input")
                        val parsedValue = input.toDoubleOrNull()

                        if (parsedValue != null) {
                            editedLatitude = parsedValue
                        }
                    },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = editedLongitude.toString(),
                    onValueChange = { input ->
                        val parsedValue = input.toDoubleOrNull()
                        if (parsedValue != null) {
                            editedLongitude = parsedValue
                        }
                    },
                    label = { Text("Longitude") },
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
                        currentLocation?.let { (latitude, longitude) ->
                            // Only update if `isUpdatingLocation` is true

                            editedLatitude = latitude
                            editedLongitude = longitude
                            isUpdatingLocation = false // Reset after updating
                        }

                    }
                } else {
                    Button(onClick = {
                        gardenScreenViewModel.requestLocationPermission(activity, permissionLauncher)
                    }) {
                        Text("Request Location Permission")
                    }
                }

                Row {
                    Button(onClick = {
                        gardenScreenViewModel.updateGarden(garden.id, garden.copy(
                            name = editedName,
                            dimension = editedDimension,
                            latitude = editedLatitude,
                            longitude = editedLongitude
                        ))
                        isEditing = false
                    }) {
                        Text("Save")
                    }
                    Button(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                // View mode
                Text(text = "Name of the garden: ${garden.name}", style = MaterialTheme.typography.titleLarge)
                Text(text = "Dimension: ${garden.dimension} sqm", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Location: (${garden.latitude}, ${garden.longitude})", style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PhotosCard(photos = garden.photos)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (!showCamera) showCamera = true
                    }) {
                        Text("Add Photo")
                    }
                }

                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }
            }
        }
    }
    if (showCamera) {
        CameraButton(
            gardenScreenViewModel = gardenScreenViewModel,
            garden.id,
            0L, // unused param
            onDismiss = { showCamera = false },
            onSavePhotoClick = { garden_Id, plant_Id, _photos ->
                gardenScreenViewModel.updateGarden(garden_Id, garden.copy(
                    name = garden.name,
                    dimension = garden.dimension,
                    latitude = garden.latitude,
                    longitude = garden.longitude,
                    photos = garden.photos + _photos
                ))
            }
        )
    }
}

/*@Composable
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
}*/

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
