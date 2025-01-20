package com.example.gardenbuddy.ui.screens.gardenplantscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreenViewModel


@Composable
fun GardenPlantScreen (
    navController: NavController,
    sharedUserViewModel: SharedUserViewModel,
    GardenplantScreenViewModel: GardenScreenViewModel = viewModel()
){

    val errorMessage by GardenplantScreenViewModel.errorMessage.collectAsState()
    val isLoading by GardenplantScreenViewModel.isLoading.collectAsState()

}