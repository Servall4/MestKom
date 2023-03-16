package com.example.mestkom.data.network

import com.example.mestkom.data.responses.LoginResponse
import com.example.mestkom.data.responses.RegisterResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

class Request internal constructor(val foo: String, val bar: String)

interface AuthApi {

    @POST("/signin")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : LoginResponse

    @POST("/signup")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String
    ) : RegisterResponse


}