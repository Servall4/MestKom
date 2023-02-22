package com.example.mestkom

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.mestkom.databinding.ActivityRegisterBinding


class activity_register : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginEditText: EditText = binding.login
        val passwordEditText: EditText = binding.password
        val confirmpasswordEditText: EditText = binding.confirmpassword
        val emailEditText: EditText = binding.email
        val createaccount: ImageButton = binding.loginbtn
        val loginerr: TextView = binding.loginerr
        val samepassworderr: TextView = binding.samepassworderr
        val emailerr: TextView = binding.emailerr
        val passwordlength: TextView = binding.passwordlength


        fun validateData(email: String, login:String, password:String, confirmpassword:String): Boolean {


            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailerr.visibility = View.VISIBLE
                return false
            } else
                emailerr.visibility = View.GONE

            if (password.length < 8){
                passwordlength.visibility = View.VISIBLE
                return false
            } else
                passwordlength.visibility = View.GONE

            if (password != confirmpassword){
                samepassworderr.visibility = View.VISIBLE
                return false
            } else
                samepassworderr.visibility = View.GONE

            if (!login.matches(Regex("^(?=.*[A-Za-z0-9]\$)[A-Za-z][A-Za-z\\d.-]{0,19}\$"))){
                loginerr.visibility = View.VISIBLE
                return false
            } else
                loginerr.visibility = View.GONE

            return true
        }

/*
        createaccount.setOnClickListener({
            var email: String = emailEditText.text.toString()
            var login: String = loginEditText.text.toString()
            var password: String = passwordEditText.text.toString()
            var confirmpassword: String = confirmpasswordEditText.text.toString()

            if (validateData(email, login, password, confirmpassword)){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener({
                    if (it.isSuccessful) {
                        val intent = Intent(this, activity_login::class.java)
                        startActivity(intent)
                    } else
                    {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                })


            }
        })
*/

    }

}