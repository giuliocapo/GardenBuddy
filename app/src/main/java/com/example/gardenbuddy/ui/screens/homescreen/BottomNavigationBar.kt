package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.painterResource
import com.example.gardenbuddy.R
import com.example.gardenbuddy.ui.screens.SharedUserViewModel


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    sharedUserViewModel: SharedUserViewModel,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.offset(y = 16.dp)

    ) {
        NavigationBarItem(
            selected = selectedTab == "home",
            onClick = {
                onTabSelected("home")
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Home", Modifier.size(26.dp)) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTab == "garden",

            onClick = {
                onTabSelected("garden")
                navController.navigate("garden") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(painterResource(id = R.drawable.ic_garden), contentDescription = "Garden", Modifier.size(26.dp)) },
            label = { Text("Garden") }
        )
        NavigationBarItem(
            selected = selectedTab == "social",
            onClick = {
                onTabSelected("social")
                navController.navigate("social") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(painterResource(id = R.drawable.ic_social), contentDescription = "Social", Modifier.size(26.dp)) },
            label = { Text("Social") }
        )
        NavigationBarItem(
            selected = selectedTab == "userProfile",
            onClick = {
                onTabSelected("userProfile")

                // Recupera userId dell’utente loggato (se esiste)
                val loggedUserId = sharedUserViewModel.user.value?.userId ?: ""
                println("Logged User ID: $loggedUserId")

                navController.navigate("userProfile/$loggedUserId") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile", Modifier.size(26.dp)) },
            label = { Text("Profile") }
        )
    }
}
