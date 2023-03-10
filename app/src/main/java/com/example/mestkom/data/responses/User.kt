package com.example.mestkom.data.responses

data class User(
    val username: String,
    val password: String,
    val email: String,
    val salt: String?,
    val id: Int
)
