package com.example.mestkom.data.network

import com.example.mestkom.data.responses.LoginResponse
import com.example.mestkom.data.responses.RegisterResponse
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

    @FormUrlEncoded
    @POST("/signup")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String
    ) : RegisterResponse


}