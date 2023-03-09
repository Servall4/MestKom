package com.example.mestkom.repository

import com.example.mestkom.network.AuthApi

class AuthRepository(
    private val api: AuthApi
): BaseRepository() {

    suspend fun login(
        username: String,
        password: String
    ) = safeApiCall {
        api.login(username, password)
    }
}