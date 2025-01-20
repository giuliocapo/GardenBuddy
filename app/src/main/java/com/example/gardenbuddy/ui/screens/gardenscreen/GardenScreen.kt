package com.example.gardenbuddy.ui.screens.gardenscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gardenbuddy.ui.screens.SharedUserViewModel

@Composable
fun GardenScreen (
    navController: NavController,
    sharedUserViewModel: SharedUserViewModel,
    GardenScreenViewModel: GardenScreenViewModel = viewModel()
){
    val errorMessage by GardenScreenViewModel.errorMessage.collectAsState()
    val isLoading by GardenScreenViewModel.isLoading.collectAsState()

}