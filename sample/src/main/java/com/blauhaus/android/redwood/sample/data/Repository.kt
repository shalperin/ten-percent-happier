package com.blauhaus.android.redwood.sample.data

import androidx.lifecycle.MutableLiveData

class Repository : IRepository {
    val _meditationData = MutableLiveData<List<Pair<Float, String>>>()

    override fun meditationData(): MutableLiveData<List<Pair<Float, String>>> {
        return _meditationData
    }

    init {
        loadData()
    }

    fun loadData() {
        _meditationData.postValue(meditationHistoryData)
    }
}

interface IRepository {
    fun meditationData(): MutableLiveData<List<Pair<Float, String>>>
}