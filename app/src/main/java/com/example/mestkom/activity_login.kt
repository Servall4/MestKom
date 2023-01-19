package com.example.mestkom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login: EditText = findViewById(R.id.login)
        val password: EditText = findViewById(R.id.password)

        val loginbtn: ImageButton = findViewById(R.id.loginbtn)
        val register: ImageButton = findViewById(R.id.registerbtn)


    }
}