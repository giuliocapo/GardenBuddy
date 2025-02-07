package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gardenbuddy.ui.screens.SharedUserViewModel


@Composable
fun HomeScreen(navController: NavHostController, sharedUserViewModel: SharedUserViewModel) {
//  val navController = rememberNavController()
    val selectedTab = remember { mutableStateOf("home") }
    val user by sharedUserViewModel.user.collectAsState()

    Scaffold(
        Modifier.fillMaxSize(),

        content = { innerPadding ->
            Column(Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(text = "Welcome to GardenBuddy, ${user!!.name}!", color = Color.Black)

                WeatherCard(
                    location = "Rome, Italy",
                    temperature = "18°C",
                    summary = "Sunny",
                    modifier = Modifier
                )

                GardeningMonitoringScreen(user!!)
            }
        },

        bottomBar = {
            BottomNavigationBar(navController = navController, sharedUserViewModel = sharedUserViewModel, selectedTab = selectedTab.value) {
                selectedTab.value = it
            }
        }
    )
}

@Composable
fun SaveSessionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Do you want to save the gardening session?") },
        text = { Text("Do you want to save the gardening session data?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview(){
    //HomeScreen("username", rememberNavController())
}

