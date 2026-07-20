package com.example.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

data class Position(
    val latitude: Double,
    val longitude: Double
)

class LocationService(private val context: Context) {

    fun isLocationPermissionGranted(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineLocation || coarseLocation
    }

    suspend fun getCurrentPosition(): Position {
        if (!isLocationPermissionGranted()) {
            throw Exception("Location permissions are denied.")
        }
        
        // Return standard Nairobi coordinates (Kenyan context) as the baseline current location.
        // In real-world usage we'd use FusedLocationProviderClient.
        return Position(
            latitude = -1.286389,
            longitude = 36.817223
        )
    }
}
