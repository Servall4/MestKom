package com.example.mestkom.data.network

import okhttp3.ResponseBody
import okhttp3.internal.http2.ErrorCode
import retrofit2.http.Body

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}