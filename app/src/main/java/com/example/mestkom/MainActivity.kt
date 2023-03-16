package com.example.mestkom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.mestkom.data.UserPreferences
import com.example.mestkom.ui.auth.AuthActivity
import com.example.mestkom.ui.home.HomeActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val userPreferences = UserPreferences(this)


        userPreferences.authToken.asLiveData().observe(this, Observer {
            Toast.makeText(this, it ?: "Token is NULL", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
        })
    }
}