package com.example.mestkom.data.repository

import com.example.mestkom.data.UserPreferences
import com.example.mestkom.data.network.AuthApi
import com.example.mestkom.data.network.RequestModel
import org.json.JSONObject

class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
): BaseRepository() {

    suspend fun login(
        username: String,
        password: String
    ) = safeApiCall {
        api.login(RequestModel(username, password, null))
    }

    suspend fun register(
        username: String,
        password: String,
        email: String
    ) = safeApiCall {
        api.register(RequestModel(username, password, email))
    }

    suspend fun saveAuthToken(token: String) {
        preferences.saveAuthToken(token)
    }
}