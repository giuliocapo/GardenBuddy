package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val navController = rememberNavController()
    val selectedTab = remember { mutableStateOf("home") }


    Scaffold(

        bottomBar = {
            BottomNavigationBar(navController = navController, selectedTab = selectedTab.value) {
                selectedTab.value = it
            }
        }
    ) { innerPadding ->
        NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
    Column {
        Text(text = "Welcome to GardenBuddy!")
        Button(onClick = { navController.navigate("plant_details") }) {
            Text("Go to plant_details")
        }
    }
}

