package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.gardenbuddy.ui.screens.SharedUserViewModel


@Composable
fun HomeScreen(navController: NavHostController, sharedUserViewModel: SharedUserViewModel) {
//    val navController = rememberNavController()
    val selectedTab = remember { mutableStateOf("home") }
    val user by sharedUserViewModel.user.collectAsState()

    Scaffold(
        Modifier.fillMaxSize(),

        content = { innerPadding ->
            Column(Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(text = "Welcome to GardenBuddy, ${user!!.name}!")

                WeatherCard(
                    location = "Rome, Italy",
                    temperature = "18°C",
                    summary = "Sunny",
                    modifier = Modifier
                )
            }
        },

        bottomBar = {
            BottomNavigationBar(navController = navController, sharedUserViewModel = sharedUserViewModel, selectedTab = selectedTab.value) {
                selectedTab.value = it
            }
        }
    )
}



@Preview
@Composable
fun HomeScreenPreview(){
    //HomeScreen("username", rememberNavController())
}

