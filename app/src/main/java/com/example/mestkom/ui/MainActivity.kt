package com.example.mestkom.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mestkom.R
import com.example.mestkom.ui.auth.AuthActivity
import com.example.mestkom.ui.home.HomeActivity


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