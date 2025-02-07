package com.example.gardenbuddy.utils

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import kotlin.math.sqrt


object LocationUtils {

    @Composable
    fun rememberLocationPermissionLauncher(
        onResult: (Boolean) -> Unit
    ): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val areGranted = permissions.entries.all { it.value }
            onResult(areGranted)
        }
    }

    fun requestLocationPermissions(
        permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        // Always launch permission request, regardless of previous denials
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    suspend fun getCurrentLocation(
        activity: ComponentActivity
    ): Pair<Double, Double>? {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If no permission, return null
            return null
        }

        // Get location if permission is granted
        return suspendCancellableCoroutine { continuation ->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        continuation.resume(Pair(it.latitude, it.longitude))
                    } ?: continuation.resume(null)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }


    class GardeningMonitoringService : Service(), SensorEventListener {

        private val binder = LocalBinder()
        private lateinit var sensorManager: SensorManager
        private var accelerometer: Sensor? = null

        // Dati del monitoraggio
        var steps: Int = 0
            private set
        var kcalBurned: Double = 0.0
            private set

        // Tempo di inizio del monitoraggio
        var sessionStartTime: Long = 0
            private set

        // Variabili per il debounce
        private var lastStepTime: Long = 0

        // Variabili per il filtro passa basso
        private val gravity = FloatArray(3) { 0f }
        private val alpha = 0.8f

        inner class LocalBinder : Binder() {
            fun getService(): GardeningMonitoringService = this@GardeningMonitoringService
        }

        override fun onBind(intent: Intent?): IBinder = binder

        override fun onCreate() {
            super.onCreate()
            // Salva il tempo di inizio della sessione
            sessionStartTime = System.currentTimeMillis()

            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            accelerometer?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
            startForegroundServiceNotification()
        }

        override fun onDestroy() {
            sensorManager.unregisterListener(this)
            super.onDestroy()
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return

            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                // Filtro passa basso per isolare la gravità
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

                // Calcola l'accelerazione lineare rimuovendo la gravità
                val linearAccelerationX = event.values[0] - gravity[0]
                val linearAccelerationY = event.values[1] - gravity[1]
                val linearAccelerationZ = event.values[2] - gravity[2]

                val magnitude = sqrt(
                    linearAccelerationX * linearAccelerationX +
                            linearAccelerationY * linearAccelerationY +
                            linearAccelerationZ * linearAccelerationZ
                )

                // Soglia per ridurre la sensibilità
                val threshold = 4.0f
                val currentTime = System.currentTimeMillis()
                if (magnitude > threshold && currentTime - lastStepTime > 300) {
                    steps++
                    kcalBurned = steps * 0.04
                    lastStepTime = currentTime
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

        private fun startForegroundServiceNotification() {
            val channelId = "gardening_monitoring_channel"
            val channelName = "Gardening Monitoring"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
                )
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }

            val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(this, channelId)
                    .setContentTitle("Monitoring in corso")
                    .setContentText("La sessione di gardening è attiva")
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .build()
            } else {
                Notification.Builder(this)
                    .setContentTitle("Monitoring in corso")
                    .setContentText("La sessione di gardening è attiva")
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .build()
            }

            startForeground(1, notification)
        }
    }
}