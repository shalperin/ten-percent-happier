package com.blauhaus.android.redwood.sample.data

import androidx.lifecycle.MutableLiveData

class Repository : IRepository {
    val _meditationData = MutableLiveData<List<Pair<Float, String>>>()
    val _globalStats = MutableLiveData<List<Int>>()

    override fun meditationData(): MutableLiveData<List<Pair<Float, String>>> {
        return _meditationData
    }

    override fun globalStats() : MutableLiveData<List<Int>> {
        return _globalStats
    }

    init {
        loadData()
    }

    fun loadData() {
        _meditationData.postValue(meditationHistoryData)
        _globalStats.postValue(globalStats)
    }

    companion object {
        val GLOBAL_DATA_PARTICIPANTS = 0
        val GLOBAL_DATA_MINUTES = 1
        val GLOBAL_DATA_AVERAGE = 2
    }
}

interface IRepository {
    fun meditationData(): MutableLiveData<List<Pair<Float, String>>>
    fun globalStats() : MutableLiveData<List<Int>>
}