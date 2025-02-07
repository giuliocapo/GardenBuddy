package com.example.gardenbuddy.ui.screens.homescreen
import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gardenbuddy.data.models.User
import kotlinx.coroutines.delay


@Composable
fun GardeningMonitoringScreen(user: User)
{
    // Recupera l'application dal LocalContext
    val application = LocalContext.current.applicationContext as Application
    // Crea il factory per il ViewModel
    val viewModelFactory = remember { GardeningMonitoringViewModelFactory(application, user) }
    val viewModel: GardeningMonitoringViewModel = viewModel(factory = viewModelFactory, viewModelStoreOwner = LocalContext.current as androidx.activity.ComponentActivity)
    val isMonitoringActive by viewModel.isMonitoringActive.observeAsState(false)
    val steps by viewModel.steps.observeAsState(0)
    val kcalBurned by viewModel.kcalBurned.observeAsState(0.0)
    val sessionStartTime by viewModel.sessionStartTime.observeAsState(null)


    if (isMonitoringActive) {
        GardeningSessionCard(
            steps = steps,
            kcalBurned = kcalBurned,
            sessionStartTime = sessionStartTime,
            onStopMonitoring = { viewModel.stopMonitoring() }
        )
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp,
            shape = MaterialTheme.shapes.medium
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.startMonitoring() }) {
                    Text("Start Monitoring")
                }
            }
        }
    }
}


@Composable
fun GardeningSessionCard(
    steps: Int,
    kcalBurned: Double,
    sessionStartTime: Long?,
    onStopMonitoring: () -> Unit
) {
    // Calcola il tempo trascorso, se disponibile
    val elapsedTime by produceState(initialValue = 0L, sessionStartTime) {
        while (true) {
            sessionStartTime?.let {
                value = (System.currentTimeMillis() - it) / 1000
            }
            delay(1000)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Gardening Session", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Steps: $steps", style = MaterialTheme.typography.body1)
            Text(text = "Kcal Burned: %.2f".format(kcalBurned), style = MaterialTheme.typography.body1)
            Text(text = "Time: ${elapsedTime}s", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onStopMonitoring) {
                Text("Stop Session")
            }
        }
    }
}







