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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gardenbuddy.ui.screens.LogInScreen
import com.example.gardenbuddy.ui.screens.SignUpScreen
import com.example.gardenbuddy.ui.screens.UserProfileScreen
import com.example.gardenbuddy.ui.screens.homescreen.HomeScreen
import com.example.gardenbuddy.ui.theme.GardenBuddyTheme
import com.example.gardenbuddy.utils.FirebaseInitializer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inizializza Firebase
        FirebaseInitializer.init(this)

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
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "logIn",
        modifier = modifier
    ) {
        composable("logIn") {
            LogInScreen(navController)
        }
        composable("signUp") {
            SignUpScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("userProfile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserProfileScreen(userId = userId)
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
