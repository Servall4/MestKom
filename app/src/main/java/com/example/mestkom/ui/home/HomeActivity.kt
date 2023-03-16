package com.example.mestkom.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.example.mestkom.R
import com.example.mestkom.databinding.ActivityAuthBinding
import com.example.mestkom.databinding.ActivityHomeBinding
import com.example.mestkom.databinding.FragmentHomeBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

lateinit var binding: ActivityHomeBinding



class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("544fcc81-0d9f-43db-8e6c-0966ab04f5fc")
        MapKitFactory.initialize(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}