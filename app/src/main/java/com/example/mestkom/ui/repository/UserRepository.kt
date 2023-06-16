package com.example.mestkom.ui.repository


import com.example.mestkom.data.network.UserApi
import com.example.mestkom.data.network.UserRequestModel

class UserRepository(
    private val api: UserApi
): BaseRepository() {

    suspend fun getVideos() = safeApiCall {
        api.getVideos()
    }
    suspend fun getUser(id: String) = safeApiCall {
        api.getUser(UserRequestModel(id))
    }

}