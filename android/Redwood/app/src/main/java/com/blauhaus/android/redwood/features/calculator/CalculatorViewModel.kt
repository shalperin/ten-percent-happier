package com.blauhaus.android.redwood.features.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalculatorViewModel(val repo:CalculatorRepo) : ViewModel() {
    val resultLdMap = mutableMapOf<Int, MutableLiveData<Float>>()
    val errorMessageLdMap = mutableMapOf<Int, MutableLiveData<String>>()
    val loadingLdMap = mutableMapOf<Int, MutableLiveData<Boolean>>()


    fun sum(id:Int, a:Float, b:Float) {
        GlobalScope.launch {
            loading(id)?.postValue(true)
            try {
                var response = repo.sum(a, b)
                result(id)?.postValue(response.sum)
            } catch (e:Exception) {
                errorMessage(id)?.postValue(e.message)
            } finally {
                loading(id)?.postValue(false)
            }
        }
        //TODO: See this doc for another approach that lets you peel out the response status code.
        // https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter/issues/16
    }

    fun result(id: Int): MutableLiveData<Float>? {
        if (! resultLdMap.containsKey(id)) {
            resultLdMap.put(id, MutableLiveData<Float>())
        }
        return resultLdMap.get(id)
    }

    fun errorMessage(id:Int) : MutableLiveData<String>? {
        if (! errorMessageLdMap.containsKey(id)) {
            errorMessageLdMap.put(id, MutableLiveData<String>())
        }
        return errorMessageLdMap.get(id)
    }

    fun loading(id:Int) : MutableLiveData<Boolean>? {
        if (! loadingLdMap.containsKey(id)) {
            loadingLdMap.put(id, MutableLiveData<Boolean>())
        }
        return loadingLdMap.get(id)
    }

}