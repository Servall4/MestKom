package com.example.mestkom.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.mestkom.R
import com.example.mestkom.data.PreferencesManager
import com.example.mestkom.ui.auth.AuthActivity
import com.example.mestkom.ui.home.HomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        viewModel = MainViewModel(PreferencesManager.Base(getSharedPreferences("pref",Context.MODE_PRIVATE)))
        viewModel.isSignIn(applicationContext)
        viewModel.isSignIn.observe(this) {
            val activity = if (it) HomeActivity::class.java else AuthActivity::class.java
            startNewActivity(activity)
            finish()
        }

        if (!Build.HARDWARE.equals("ranchu")) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            if (savedInstanceState == null) {
                getLocation()
            }
        }
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) return true

        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "You have the Permission")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Log.d("TAG", "LOCATION IS NULL")
                    } else {
                        val lat = location.latitude
                        val long = location.longitude
                        viewModel.saveLocation(lat, long)
                    }
                }
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_LONG)
                    .show()
            }
        } else requestPermissions()
    }


    private  companion object {
        const val PERMISSION_ID = 100
    }
}