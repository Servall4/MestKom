package com.example.mestkom.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val TAG = "PreferenceManager"

class PreferenceManager(private val sharedPreferences: SharedPreferences) {

    private val location: MutableLiveData<Pair<Float, Float>> = MutableLiveData()

    @SuppressLint("CommitPrefEdits")
    fun saveLocation(lat: Float, lon: Float) {
        Log.d(TAG, "Save location: $lat / $lon")
        sharedPreferences.edit().putFloat(LAT_KEY, lat).apply()
        sharedPreferences.edit().putFloat(LON_KEY, lon).apply()
    }

    fun getLocation(): LiveData<Pair<Float, Float>> {
        val lat = sharedPreferences.getFloat(LAT_KEY, 37.7739F)
        val lon = sharedPreferences.getFloat(LON_KEY, -122.4312F)
        Log.d(TAG, "Get location: $lat /$lon")
        location.postValue(Pair(lat, lon))
        return location
    }
    private companion object {
        private const val LAT_KEY = "LAT_KEY"
        private const val LON_KEY = "LON_KEY"
    }
}