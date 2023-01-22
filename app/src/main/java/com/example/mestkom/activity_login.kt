package com.example.mestkom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.mestkom.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class activity_login : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()


        val login: EditText = binding.login
        val password: EditText = binding.password

        val loginbtn: ImageButton = binding.loginbtn
        val registerbtn: ImageButton = binding.registerbtn

        register.setOnClickListener({
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        })

    }
}