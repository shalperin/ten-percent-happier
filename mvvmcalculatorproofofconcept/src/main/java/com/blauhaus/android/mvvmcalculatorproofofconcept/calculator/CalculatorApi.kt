package com.blauhaus.android.mvvmcalculatorproofofconcept.calculator

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CalculatorApi {
    @POST("/add")
    @FormUrlEncoded
    suspend fun postAdd(@Field("a") a:Float, @Field("b") b:Float): CalculatorModel
}

data class CalculatorModel(val sum:Float) {}

class CalculatorAuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val requestUrl = req?.url
        // TODO: Should we need this: DONT INCLUDE API KEYS IN YOUR SOURCE CODE
        val url = requestUrl.newBuilder().addQueryParameter("APPID", "your_key_here").build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}
