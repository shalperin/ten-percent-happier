package com.blauhaus.android.redwood.features.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalculatorViewModel(val repo:CalculatorRepo) : ViewModel() {
    // Mapping instance ID to a LiveData for serving up the result of computation
    private val resultLdMap = mutableMapOf<Int, MutableLiveData<Float>>()

    // Mapping instance ID to a LiveData for surfacing error messages
    private val errorMessageLdMap = mutableMapOf<Int, MutableLiveData<String>>()

    // Mapping instance ID to a LiveData for driving a loading spinner
    private val loadingLdMap = mutableMapOf<Int, MutableLiveData<Boolean>>()

    // A stream of result data differentiated by instance ID
    fun result(id: Int): MutableLiveData<Float>? {
        if (! resultLdMap.containsKey(id)) {
            resultLdMap.put(id, MutableLiveData<Float>())
        }
        return resultLdMap.get(id)
    }

    // Retrieve a LiveData based on instance ID.
    fun errorMessage(id:Int) : MutableLiveData<String>? {
        if (! errorMessageLdMap.containsKey(id)) {
            errorMessageLdMap.put(id, MutableLiveData<String>())
        }
        return errorMessageLdMap.get(id)
    }

    // Same
    fun loading(id:Int) : MutableLiveData<Boolean>? {
        if (! loadingLdMap.containsKey(id)) {
            loadingLdMap.put(id, MutableLiveData<Boolean>())
        }
        return loadingLdMap.get(id)
    }

    // Same
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


}