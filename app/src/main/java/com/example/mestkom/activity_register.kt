package com.example.mestkom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ThreadContextElement

class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val loginEditText: EditText = findViewById(R.id.login)
        val passwordEditText: EditText = findViewById(R.id.password)
        val confirmpasswordEditText: EditText = findViewById(R.id.confirmpassword)
        val emailEditText: EditText = findViewById(R.id.email)
        val createaccount: ImageButton = findViewById(R.id.loginbtn)
        val loginerr: TextView = findViewById(R.id.loginerr)
        val passworderr: TextView = findViewById(R.id.samepassworderr)
        val emailerr: TextView = findViewById(R.id.emailerr)




        fun createAccount(){

            var email: String = emailEditText.text.toString()
            var login: String = loginEditText.text.toString()
            var password: String = passwordEditText.text.toString()
            var confirmpassword: String = confirmpasswordEditText.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailerr.visibility = View.VISIBLE
                return
            } else
                emailerr.visibility = View.INVISIBLE

            if (password != confirmpassword){
                passworderr.visibility = View.VISIBLE
                return
            } else
                passworderr.visibility = View.INVISIBLE

            if (!login.matches(Regex("^(?=.*[A-Za-z0-9]\$)[A-Za-z][A-Za-z\\d.-]{0,19}\$"))){
                loginerr.visibility = View.VISIBLE
                return
            } else
                loginerr.visibility = View.INVISIBLE



        }




    }
}