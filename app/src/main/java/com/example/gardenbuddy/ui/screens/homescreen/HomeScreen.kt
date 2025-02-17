package com.example.gardenbuddy.ui.screens.homescreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gardenbuddy.ui.screens.SharedUserViewModel

@Composable
fun HomeScreen(navController: NavHostController, sharedUserViewModel: SharedUserViewModel, weatherViewModel : WeatherViewModel = hiltViewModel()) {
//  val navController = rememberNavController()
    val selectedTab = remember { mutableStateOf("home") }
    val user by sharedUserViewModel.user.collectAsState()
    val weatherInfo by weatherViewModel.weatherInfo.collectAsState()
    val weatherIconCode by weatherViewModel.weatherIconCode.collectAsState()

    Scaffold(
        Modifier.fillMaxSize(),

        content = { innerPadding ->
            Column(Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Row(Modifier.fillMaxWidth().padding(16.dp)){
                    Text(text = "Welcome to GardenBuddy, ${user!!.name}!", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
                weatherInfo?.let {
                    weatherIconCode?.let { code ->
                        Log.d("WeatherCode", code.toString())
                        if (weatherInfo!!.description == ""){

                            val gradient = Brush.verticalGradient(
                                colors = listOf(Color(0xFF35509B), Color(0xFF60A5FA))
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
                                    .background(brush = gradient, shape = RoundedCornerShape(16.dp))
                                    .height(140.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text("Create your garden to see weather data",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold)
                            }
                        }else{
                            WeatherCard(
                                location = it.location,
                                temperature = "${it.temperature.toInt()}°C",
                                summary = it.description,
                                weatherIcon = code,
                                modifier = Modifier
                            )
                        }

                    }

                    GardeningMonitoringScreen(user!!, Modifier.weight(1f))
                } ?:
                Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    CircularProgressIndicator()
                }
            }
        },

        bottomBar = {
            BottomNavigationBar(navController = navController, sharedUserViewModel = sharedUserViewModel, selectedTab = selectedTab.value) {
                selectedTab.value = it
            }
        }
    )
}

//@Composable
//fun SaveSessionDialog(
//    onDismiss: () -> Unit,
//    onConfirm: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Do you want to save the gardening session?") },
//        text = { Text("Do you want to save the gardening session data?") },
//        confirmButton = {
//            TextButton(onClick = onConfirm) {
//                Text("Save")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}

@Preview
@Composable
fun HomeScreenPreview(){
    //HomeScreen("username", rememberNavController())
}

