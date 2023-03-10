package com.example.mestkom.ui.auth

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.mestkom.R
import com.example.mestkom.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.root);
        supportActionBar?.hide()
    }
}