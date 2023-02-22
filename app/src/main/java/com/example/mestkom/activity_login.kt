package com.example.mestkom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.mestkom.databinding.ActivityLoginBinding
import com.example.mestkom.databinding.ActivityRegisterBinding
import kotlin.math.log

class activity_login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val loginEditText: EditText = binding.login
        val passwordEditText: EditText = binding.password
        val loginbtn: ImageButton = binding.loginbtn
        val registerbtn: ImageButton = binding.registerbtn

        fun validateData(login: String, password: String): Boolean {
            if (login.isNotEmpty() && password.isNotEmpty())
                return true
            else
                return false

        }

        registerbtn.setOnClickListener({
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        })





    }
}