package com.blauhaus.android.redwood.components.calculator

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CalculatorRepoImpl :CalculatorRepo {
    fun shortTimeoutHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(shortTimeoutHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(CalculatorService::class.java)
    }

    override suspend fun sum(a: Float, b:Float) = webservice.postAdd(a, b)
}