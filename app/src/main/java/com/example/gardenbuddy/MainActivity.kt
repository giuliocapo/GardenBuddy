package com.example.gardenbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gardenbuddy.ui.screens.homescreen.HomeScreen
import com.example.gardenbuddy.ui.theme.GardenBuddyTheme
import com.example.gardenbuddy.ui.screens.userprofilescreen.SettingsScreen
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreen
import com.example.gardenbuddy.utils.FirebaseInitializer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inizializzazione Firebase
        FirebaseInitializer.init(this)

        enableEdgeToEdge()
        setContent {
            GardenBuddyTheme {
                AppScaffold()
            }
        }
    }
}

@Composable
fun AppScaffold() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
            composable("plant_details") { GardenScreen(navController) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppScaffoldPreview() {
    GardenBuddyTheme {
        AppScaffold()
    }
}
