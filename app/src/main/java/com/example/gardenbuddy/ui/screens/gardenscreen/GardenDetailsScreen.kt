package com.example.gardenbuddy.ui.screens.gardenscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.gardenplantscreen.GardenPlantScreen
import com.example.gardenbuddy.ui.screens.homescreen.BottomNavigationBar
import com.example.gardenbuddy.ui.screens.plantScreen.PlantScreenViewModel
import com.example.gardenbuddy.ui.screens.plantScreen.PlantSearchSection


@Composable
fun GardenDetailsScreen(
    navController: NavHostController,
    sharedUserViewModel: SharedUserViewModel,
    gardenScreenViewModel: GardenScreenViewModel = viewModel(),
    plantScreenViewModel: PlantScreenViewModel = viewModel()
) {
    val gardenPlants by gardenScreenViewModel.gardenplantsSuccess.observeAsState(emptyList())
    val gardenPlantsIsLoading by gardenScreenViewModel.gardenPlantsisLoading.collectAsState()
    val selectedTab = remember { mutableStateOf("garden") }
    val currentEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val gardenId = currentEntry?.arguments?.getString("id")?.toLongOrNull()

    // Carica i dati
    LaunchedEffect(gardenId) {
        if(gardenId != null){
            gardenScreenViewModel.loadGardenPlants(gardenId)
        }
    }

    Scaffold(
    bottomBar = {
        BottomNavigationBar(
            navController = navController,
            sharedUserViewModel = sharedUserViewModel,
            selectedTab = selectedTab.value
        ) { tab ->
            selectedTab.value = tab
            navController.navigate(tab)
        }
    },
    content = { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if(gardenId != null){
                //Plant Search Section
                PlantSearchSection(plantScreenViewModel = plantScreenViewModel, gardenId, gardenScreenViewModel)

                Spacer(modifier = Modifier.height(8.dp))

                if (gardenPlantsIsLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    gardenPlants?.let { plants ->
                        GardenPlantScreen(
                            gardenPlants = plants,
                            gardenScreenViewModel = gardenScreenViewModel,
                            gardenId
                        )

                    }
                }
            }
        }
    })
}
