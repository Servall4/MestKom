package com.example.mestkom.data.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming

interface FileApi {
    @Multipart
    @POST("/uploadVideo")
    suspend fun uploadVideo(
        @Part video: MultipartBody.Part,
        @Part info: MultipartBody.Part
    ): ResponseBody

    @Streaming
    @POST("/getVideo")
    suspend fun downloadVideo(
        @Body fileRequestModel: VideoRequestModel
    ): ResponseBody
}

