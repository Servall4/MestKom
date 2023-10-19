package com.example.mestkom.data.location

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import com.example.mestkom.data.PreferenceManager
import java.util.concurrent.ExecutorService

private const val TAG = "LocationRepository"

class LocationRepository(
    private val locationManager: LocationManager,
    private val locationStorage: PreferenceManager,
    private val executor: ExecutorService
) {

    /// Returns location from repository
    fun getLocation(): LiveData<Pair<Float, Float>> = locationStorage.getLocation()

    val receivingLocationUpdates: LiveData<Boolean> = locationManager.receivingLocationUpdates

    /// Saves location to repository
    fun updateLocation(location: Pair<Float, Float>) {
        executor.execute {
            locationStorage.saveLocation(location.first, location.second)
        }
    }

    @MainThread
    fun startLocationUpdates() = locationManager.startLocationUpdates()

    @MainThread
    fun stopLocationUpdates() = locationManager.stopLocationUpdates()

    companion object {
        @Volatile private var INSTANCE: LocationRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): LocationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationRepository(
                    LocationManager.getInstance(context),
                    PreferenceManager(context.getSharedPreferences("pref", Context.MODE_PRIVATE)),
                    executor
                )
                    .also { INSTANCE = it }
            }
        }
    }
}