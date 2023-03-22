package com.example.mestkom.data.responses

import android.util.JsonToken

data class LoginResponse(
    val id: String,
    val username: String,
    val email: String,
    val token: String
)
