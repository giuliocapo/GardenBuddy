package com.example.gardenbuddy.ui.screens.homescreen

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
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
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Home") },
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
            icon = { Icon(Icons.Rounded.Star, contentDescription = "Garden") },
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
            icon = { Icon(Icons.Filled.Info, contentDescription = "Social") },
            label = { Text("Social") }
        )
        NavigationBarItem(
            selected = selectedTab == "profile",
            onClick = {
                onTabSelected("profile")
                navController.navigate("profile") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") { Text("Home Screen", modifier = Modifier.padding(16.dp)) }
        composable("garden") { Text("Garden Screen", modifier = Modifier.padding(16.dp)) }
        composable("social") { Text("Social Screen", modifier = Modifier.padding(16.dp)) }
        composable("profile") { Text("Profile Screen", modifier = Modifier.padding(16.dp)) }
    }
}
