package com.blauhaus.android.redwood.components.calculator

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface CalculatorRepo {
    suspend fun sum(a: Float, b:Float): CalculatorModel
}