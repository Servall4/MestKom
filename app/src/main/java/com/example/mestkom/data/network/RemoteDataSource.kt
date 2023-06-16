package com.example.mestkom.data.network

import com.example.mestkom.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RemoteDataSource {
    companion object {
        const val BASE_URL = "http://5.142.71.59:8080/"
    }

    fun<Api> buildApi(
        api: Class<Api>
    ): Api {
        return Retrofit.Builder().baseUrl(BASE_URL).client(OkHttpClient.Builder().addInterceptor{ chain ->  
            chain.proceed(chain.request().newBuilder().build())
        }.also { client ->
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }
}