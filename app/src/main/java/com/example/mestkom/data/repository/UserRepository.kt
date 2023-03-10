package com.example.mestkom.data.repository

import com.example.mestkom.data.UserPreferences
import com.example.mestkom.data.network.AuthApi
import com.example.mestkom.data.network.UserApi

class UserRepository(
    private val api: UserApi
): BaseRepository() {
    suspend fun getUser() = safeApiCall {
        api.getUser()
    }
}