package com.example.mestkom.responses

data class User(
    val username: String,
    val password: String,
    val email: String,
    val salt: String,
    val id: Int
)
