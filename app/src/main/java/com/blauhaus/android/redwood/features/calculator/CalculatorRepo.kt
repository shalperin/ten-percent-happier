package com.blauhaus.android.redwood.features.calculator

interface CalculatorRepo {
    suspend fun sum(a: Float, b:Float): CalculatorModel
}

class CalculatorRepoImpl(private val calculatorApi: CalculatorApi) :CalculatorRepo {
    override suspend fun sum(a: Float, b:Float) = calculatorApi.postAdd(a, b)
}