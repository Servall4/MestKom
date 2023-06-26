package com.example.mestkom.data.network

import com.example.mestkom.data.responses.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/signin")
    suspend fun login(
        @Body requestModel: RequestModel
    ) : AuthResponse

    @POST("/signup")
    suspend fun register(
        @Body requestModel: RequestModel
    ) : AuthResponse
}