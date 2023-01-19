package com.example.mestkom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val login: EditText = findViewById(R.id.login)
        val password: EditText = findViewById(R.id.password)
        val confirmpassword: EditText = findViewById(R.id.confirmpassword)
        val email: EditText = findViewById(R.id.email)
        val createaccount: ImageButton = findViewById(R.id.loginbtn)



    }
}