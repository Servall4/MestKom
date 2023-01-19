package com.example.mestkom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val loginEditText: EditText = findViewById(R.id.login)
        val passwordEditText: EditText = findViewById(R.id.password)
        val confirmpasswordEditText: EditText = findViewById(R.id.confirmpassword)
        val emailEditText: EditText = findViewById(R.id.email)
        val createaccount: ImageButton = findViewById(R.id.loginbtn)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.error))



        fun createAccount(){

            var email: String = emailEditText.text.toString()
            var login: String = loginEditText.text.toString()
            var password: String = passwordEditText.text.toString()
            var confirmpassword: String = confirmpasswordEditText.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                builder.setMessage(R.string.error)
                return
            }

            if (password != confirmpassword){

            }


        }




    }
}