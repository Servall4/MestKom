package com.example.mestkom.data.network

import com.example.mestkom.data.responses.UpdateResponse
import com.example.mestkom.data.responses.User
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @POST("user")
    suspend fun getUser(
        @Body userRequestModel: UserRequestModel
    ): User
    @GET("authenticate")
    suspend fun isAuth(): ResponseBody

    @GET("update")
    suspend fun getVideos(): List<UpdateResponse>

    @POST("logout")
    suspend fun logout(): ResponseBody

}