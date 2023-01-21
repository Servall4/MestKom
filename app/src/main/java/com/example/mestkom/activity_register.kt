package com.example.mestkom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ThreadContextElement

class activity_register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;


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
        val passwordlength: TextView = findViewById(R.id.passwordlength)


        fun createAccountInFirebase(email: String, login:String, password: String): Unit {

            return
        }




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
                passworderr.visibility = View.VISIBLE
                return false
            } else
                passworderr.visibility = View.GONE

            if (!login.matches(Regex("^(?=.*[A-Za-z0-9]\$)[A-Za-z][A-Za-z\\d.-]{0,19}\$"))){
                loginerr.visibility = View.VISIBLE
                return false
            } else
                loginerr.visibility = View.GONE

            return true
        }

        fun createAccount(): Unit {

            var email: String = emailEditText.text.toString()
            var login: String = loginEditText.text.toString()
            var password: String = passwordEditText.text.toString()
            var confirmpassword: String = confirmpasswordEditText.text.toString()

            var isValidated: Boolean = validateData(email, login, password, confirmpassword)

            if (!isValidated)
                return

            createAccountInFirebase(email, login, password)
        }

    }
}