package com.example.gardenbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gardenbuddy.ui.screens.loginscreen.LogInScreen
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.SignUpScreen
import com.example.gardenbuddy.ui.screens.UserProfileScreen
import com.example.gardenbuddy.ui.screens.activityboardscreen.ActivityBoardScreen
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenDetailsScreen
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreen
import com.example.gardenbuddy.ui.screens.gardenscreen.GardenScreenViewModel
import com.example.gardenbuddy.ui.screens.homescreen.HomeScreen
import com.example.gardenbuddy.ui.screens.plantScreen.PlantScreenViewModel
import com.example.gardenbuddy.ui.screens.plantScreen.PlantSearchSection
import com.example.gardenbuddy.ui.theme.GardenBuddyTheme
import com.example.gardenbuddy.utils.FirebaseInitializer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Inizializza Firebase
        FirebaseInitializer.init(this)

        setContent {
            // Usa darkTheme = false se desideri il tema chiaro
            GardenBuddyTheme(darkTheme = false) {
                AppScaffold()
            }
        }
    }
}

@Composable
fun AppScaffold(sharedUserViewModel: SharedUserViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            sharedUserViewModel = sharedUserViewModel
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier, sharedUserViewModel: SharedUserViewModel,
                   gardenScreenViewModel: GardenScreenViewModel = viewModel(),
                   plantScreenViewModel: PlantScreenViewModel = viewModel()) {
    NavHost(
        navController = navController,
        startDestination = "logIn",
        modifier = modifier
    ) {
        composable("logIn") {
            LogInScreen(navController, sharedUserViewModel = sharedUserViewModel)
        }
        composable("signUp") {
            SignUpScreen(navController, sharedUserViewModel = sharedUserViewModel)
        }
        composable("home") {
            HomeScreen(navController = navController, sharedUserViewModel = sharedUserViewModel)
        }
        composable(
            route = "userProfile/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    // Se vuoi un valore di default, lo metti qui
                    // defaultValue = "qualcosa"
                }
            )
        ) { backStackEntry ->
            val userIdArg = backStackEntry.arguments?.getString("userId") ?: ""

            // Passi userIdArg alla tua screen
            UserProfileScreen(
                navController = navController,
                sharedUserViewModel = sharedUserViewModel,
                displayedUserId = userIdArg
            )
        }
        composable("garden") {
            GardenScreen(navController = navController, sharedUserViewModel = sharedUserViewModel)
        }
        composable("social") {
            ActivityBoardScreen(navController = navController, sharedUserViewModel = sharedUserViewModel)
            Text("Social screen")
        }
        composable("garden_details/{id}") {
            GardenDetailsScreen(navController = navController, sharedUserViewModel = sharedUserViewModel, gardenScreenViewModel = gardenScreenViewModel, plantScreenViewModel = plantScreenViewModel)
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
