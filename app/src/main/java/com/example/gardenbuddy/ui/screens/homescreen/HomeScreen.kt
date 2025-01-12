package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column {
        Text(text = "Welcome to GardenBuddy!")
        Button(onClick = { navController.navigate("settings") }) {
            Text("Go to Settings")
        }
    }
}
