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
        activity: ComponentActivity,
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
}