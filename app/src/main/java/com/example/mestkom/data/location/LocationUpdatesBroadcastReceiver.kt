package com.example.mestkom.data.location

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import java.util.concurrent.Executors

private const val TAG = "LUBroadcastReceiver"


class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ACTION_PROCESS_UPDATES) {

            // Checks for location availability changes.
            LocationAvailability.extractLocationAvailability(intent)?.let { locationAvailability ->
                if (!locationAvailability.isLocationAvailable) {
                    Log.d(TAG, "Location services are no longer available!")
                }
            }

            Log.d(TAG, "WANT: Save location")

            LocationResult.extractResult(intent)?.let { locationResult ->
                val lastLocation = locationResult.locations.last()
                LocationRepository.getInstance(context, Executors.newSingleThreadExecutor())
                    .updateLocation(
                        Pair(
                            lastLocation.latitude.toFloat(),
                            lastLocation.longitude.toFloat()
                        )
                    )
            }


            Log.d(TAG, "DONE: Location saved!")
        }
    }

    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        appProcesses.forEach { appProcess ->
            if (appProcess.importance ==
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                appProcess.processName == context.packageName
            ) {
                return true
            }
        }
        return false
    }

    companion object {
        const val ACTION_PROCESS_UPDATES =
            "com.example.mestkom.action" + "PROCESS_UPDATES"
    }
}