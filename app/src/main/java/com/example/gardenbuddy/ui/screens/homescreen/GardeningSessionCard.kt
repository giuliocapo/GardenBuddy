package com.example.gardenbuddy.ui.screens.homescreen
import android.app.Application
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gardenbuddy.R
import com.example.gardenbuddy.data.models.User

@Composable
fun GardeningMonitoringScreen(user: User, modifier: Modifier) {
//     ... existing view model code remains the same ...
//     Recupera l'application dal LocalContext
    val application = LocalContext.current.applicationContext as Application
    // Crea il factory per il ViewModel
    val viewModelFactory = remember { GardeningMonitoringViewModelFactory(application, user) }
    val viewModel: GardeningMonitoringViewModel = viewModel(factory = viewModelFactory, viewModelStoreOwner = LocalContext.current as androidx.activity.ComponentActivity)
    val isMonitoringActive by viewModel.isMonitoringActive.observeAsState(false)
    val steps by viewModel.steps.observeAsState(0)
    val kcalBurned by viewModel.kcalBurned.observeAsState(0.0)
    val sessionStartTime by viewModel.sessionStartTime.observeAsState(null)
    val sessionMinutes by viewModel.sessionMinutes.observeAsState(0)
    val sessionSeconds by viewModel.sessionSeconds.observeAsState(0)

    Box( modifier.fillMaxWidth()) {
        if (isMonitoringActive) {
            GardeningSessionCard(
                steps = steps,
                kcalBurned = kcalBurned,
                sessionMinutes = sessionMinutes,
                sessionSeconds = sessionSeconds,
                onStopMonitoring = { viewModel.stopMonitoring() }
            )
        } else {
            StartMonitoringCard { viewModel.startMonitoring() }
        }
    }
}

@Composable
private fun StartMonitoringCard(onStartMonitoring: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(id = R.drawable.ic_gardening),
                contentDescription = "Garden",
                Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ready to Garden?",

                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Track your activity and progress",

            )
            Spacer(modifier = Modifier.height(24.dp))
            FilledTonalButton(
                onClick = onStartMonitoring,
                modifier = Modifier.fillMaxWidth(0.8f),

            ) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = null, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Session", style = MaterialTheme.typography.h6)
            }
        }
    }
}

@Composable
fun GardeningSessionCard(
    steps: Int,
    kcalBurned: Double,
    sessionMinutes: Int,
    sessionSeconds: Int,
    onStopMonitoring: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp)

    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Session",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically

            ) {
                MetricItem(
                    icon = Icons.AutoMirrored.Rounded.DirectionsWalk,
                    value = "$steps",
                    label = "Steps",
                    modifier = Modifier.weight(1f)
                )
                MetricItem(
                    icon = Icons.Rounded.LocalFireDepartment,
                    value = "%.1f".format(kcalBurned),
                    label = "kcal",
                    modifier = Modifier.weight(1f)
                )
                MetricItem(
                    icon = Icons.Rounded.Schedule,
                    value = "%02d:%02d".format(sessionMinutes, sessionSeconds),
                    label = "Time",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            FilledTonalButton(
                onClick = onStopMonitoring,
                modifier = Modifier.fillMaxWidth(0.8f),

                ) {
                Icon(Icons.Rounded.Stop, contentDescription = "Stop session",modifier = Modifier.size(36.dp) )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Stop Session", style = MaterialTheme.typography.h6)
            }
        }
    }
}

@Composable
private fun MetricItem(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label
        )
    }
}
//@Composable
//fun GardeningMonitoringScreen(user: User)
//{
//    // Recupera l'application dal LocalContext
//    val application = LocalContext.current.applicationContext as Application
//    // Crea il factory per il ViewModel
//    val viewModelFactory = remember { GardeningMonitoringViewModelFactory(application, user) }
//    val viewModel: GardeningMonitoringViewModel = viewModel(factory = viewModelFactory, viewModelStoreOwner = LocalContext.current as androidx.activity.ComponentActivity)
//    val isMonitoringActive by viewModel.isMonitoringActive.observeAsState(false)
//    val steps by viewModel.steps.observeAsState(0)
//    val kcalBurned by viewModel.kcalBurned.observeAsState(0.0)
//    val sessionStartTime by viewModel.sessionStartTime.observeAsState(null)
//    val sessionMinutes by viewModel.sessionMinutes.observeAsState(0)
//    val sessionSeconds by viewModel.sessionSeconds.observeAsState(0)
//
//    if (isMonitoringActive) {
//        GardeningSessionCard(
//            steps = steps,
//            kcalBurned = kcalBurned,
//            sessionMinutes = sessionMinutes,
//            sessionSeconds = sessionSeconds,
//            onStopMonitoring = { viewModel.stopMonitoring()}
//        )
//    } else {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            elevation = 8.dp,
//            shape = MaterialTheme.shapes.medium
//        ){
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Button(onClick = { viewModel.startMonitoring() }) {
//                    Text("Start Monitoring")
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun GardeningSessionCard(
//    steps: Int,
//    kcalBurned: Double,
//    sessionMinutes: Int,
//    sessionSeconds: Int,
//    onStopMonitoring: () -> Unit
//) {
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        elevation = 8.dp,
//        shape = MaterialTheme.shapes.medium
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Gardening Session", style = MaterialTheme.typography.h5)
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(text = "Steps: $steps", style = MaterialTheme.typography.body1)
//            Text(text = "Kcal Burned: %.2f".format(kcalBurned), style = MaterialTheme.typography.body1)
//            Text(text = "Time: ${sessionMinutes}:${sessionSeconds}s", style = MaterialTheme.typography.body1)
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = onStopMonitoring) {
//                Text("Stop Session")
//            }
//        }
//    }
//}







