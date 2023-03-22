package com.example.mestkom.data.responses

data class User(
    val username: String,
    val password: String,
    val email: String,
    val token: String?,
    val id: Int
)
