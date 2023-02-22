package com.example.mestkom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val goreg: Button = findViewById(R.id.goreg)
        val gologin: Button = findViewById(R.id.gologin)

        goreg.setOnClickListener({
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        })

        gologin.setOnClickListener({
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
        })


    }
}