package com.example.mestkom.network

import com.example.mestkom.responses.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("/signin")
    suspend fun login(
        @Field("username") username:String,
        @Field("password") password: String
    ) : LoginResponse


}