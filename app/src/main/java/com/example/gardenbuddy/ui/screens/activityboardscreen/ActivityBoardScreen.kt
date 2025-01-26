package com.example.gardenbuddy.ui.screens.activityboardscreen



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.homescreen.BottomNavigationBar
import com.example.gardenbuddy.ui.theme.GardenBuddyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityBoardScreen(
    navController: NavHostController,
    sharedUserViewModel: SharedUserViewModel,
    viewModel: ActivityBoardViewModel = viewModel()
) {
    val activities = viewModel.filteredActivities.collectAsState(initial = emptyList())
    val searchQuery = viewModel.searchQuery.collectAsState()

    // Stato per il tab selezionato nella barra di navigazione
    val selectedTab = remember { mutableStateOf("activityBoard") }

    val userId = sharedUserViewModel.user.value?.userId ?: ""

    // Chiamata a fetchActivities() quando la schermata viene creata
    LaunchedEffect(Unit) { // con unit essendo void lanciamo l'effetto solo quando il composable entra nella composizione una volta
        viewModel.fetchActivities()
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedTab = selectedTab.value
            ) { tab ->
                selectedTab.value = tab
                navController.navigate(tab) // Cambia schermata al cambio di tab
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Barra di ricerca
                TextField(
                    value = searchQuery.value,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Cerca utente...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                // Lista delle attività
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(activities.value) { activity ->
                        ActivityCard(activity)
                    }
                }
            }
        }
    )
}

@Composable
fun ActivityCard(activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Utente: ${activity.username}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Minuti: ${activity.minutes}",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Passi: ${activity.steps}",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Calorie: ${activity.calories} kcal",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ActivityCardPreview() {
    GardenBuddyTheme {
        // Mock activity data
        val mockActivity = Activity(
            id = "1",
            userId = "user1",
            username = "Alice",
            minutes = 30,
            steps = 5000,
            calories = 200,
            timestamp = System.currentTimeMillis()
        )
        ActivityCard(activity = mockActivity)
    }
}