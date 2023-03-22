package com.example.mestkom.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.mestkom.R
import com.example.mestkom.databinding.ActivityHomeBinding
import com.yandex.mapkit.MapKitFactory

lateinit var binding: ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("544fcc81-0d9f-43db-8e6c-0966ab04f5fc")
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavView
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation,navController)


    }
}