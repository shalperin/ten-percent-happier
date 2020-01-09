package com.blauhaus.android.redwood.components.calculator

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


//val url = "http://10.0.2.2:5000/"
val url = "https://redwood-264500.appspot.com"

data class CalculatorModel(val sum:Float) {}

interface CalculatorService {
    @POST("/add")
    @FormUrlEncoded
    suspend fun postAdd(@Field("a") a:Float, @Field("b") b:Float): CalculatorModel
}
