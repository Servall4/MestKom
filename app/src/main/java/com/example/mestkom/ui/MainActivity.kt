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
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        viewModel = MainViewModel()
        viewModel.isSignIn(applicationContext)
        viewModel.isSignIn.observe(this) {
            val activity = if (it) HomeActivity::class.java else AuthActivity::class.java
            startNewActivity(activity)
            finish()
        }
    }
}