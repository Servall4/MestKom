package com.example.mestkom.data.repository

import com.example.mestkom.data.UserPreferences
import com.example.mestkom.data.network.AuthApi
import org.json.JSONObject

class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
): BaseRepository() {

    suspend fun login(
        username: String,
        password: String
    ) = safeApiCall {
        api.login(username, password)
    }

    suspend fun register(
        username: String,
        password: String,
        email: String
    ) = safeApiCall {
        api.register(username, password, email)
    }

    suspend fun saveAuthToken(token: String) {
        preferences.saveAuthToken(token)
    }
}