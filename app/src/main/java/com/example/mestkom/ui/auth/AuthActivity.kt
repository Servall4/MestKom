package com.example.mestkom.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mestkom.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}