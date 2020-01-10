package com.blauhaus.android.redwood.features.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalculatorViewModel(val repo:CalculatorRepo) : ViewModel() {
    val result = MutableLiveData<Float>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun sum(a:Float, b:Float) {
        GlobalScope.launch {
            loading.postValue(true)
            try {
                var response = repo.sum(a, b)
                result.postValue(response.sum)
            } catch (e:Exception) {
                errorMessage.postValue(e.message)
            } finally {
                loading.postValue(false)
            }
        }
        //TODO: See this doc for another approach that lets you peel out the response status code.
        // https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter/issues/16
    }
}