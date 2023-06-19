package com.example.mestkom.ui.repository

import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.FileRequestModel
import com.example.mestkom.data.network.VideoRequestModel
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class FileRepository(
    private val api: FileApi
): BaseRepository() {

    suspend fun uploadVideo(file: File, name: String, id: String, description: String, latitude: String, longitude: String) = safeApiCall {
        val fileRequestModel = FileRequestModel(id, name, description, longitude, latitude)
        api.uploadVideo(
            video = MultipartBody.Part.createFormData(
                "video", file.name, file.asRequestBody()),
            info = MultipartBody.Part.createFormData("Info", Gson().toJson(fileRequestModel))
        )
    }

    suspend fun downloadVideo(id: String) = safeApiCall {
        api.downloadVideo(VideoRequestModel(id))
    }
}