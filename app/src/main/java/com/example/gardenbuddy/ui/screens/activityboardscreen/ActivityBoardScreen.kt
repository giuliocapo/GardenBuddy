package com.example.gardenbuddy.ui.screens.activityboardscreen



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gardenbuddy.data.models.Activity
import com.example.gardenbuddy.ui.screens.SharedUserViewModel
import com.example.gardenbuddy.ui.screens.homescreen.BottomNavigationBar
import com.example.gardenbuddy.ui.theme.GardenBuddyTheme

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
            BottomNavigationBar(navController = navController, sharedUserViewModel = sharedUserViewModel, selectedTab = selectedTab.value) {
                selectedTab.value = it
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Activities",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                // Barra di ricerca
                TextField(
                    value = searchQuery.value,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Search user...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    
                )

                // Lista delle attività
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(activities.value) { activity ->
                        ActivityCard(activity, navController)
                    }
                }
            }
        }
    )
}

@Composable
fun ActivityCard(activity: Activity, navController: NavController) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFCCDEC1), Color(0xFF8CB48D))
    )

    Box(modifier = Modifier.fillMaxWidth()
        .fillMaxWidth()
        .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
        .background(brush = gradient, shape = RoundedCornerShape(16.dp))
        .clickable { navController.navigate("userProfile/${activity.userId}") }
        ){
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row with user icon and username
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    tint = Color.Black
                )
                Text(
                    text = activity.username,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            // Row containing the three metrics with icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Time
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${activity.minutes} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
                // Steps
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DirectionsWalk,
                        contentDescription = "Steps",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${activity.steps} steps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
                // Calories
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Calories Burned",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${activity.calories} kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }
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
        ActivityCard(activity = mockActivity , navController = rememberNavController())
    }
}