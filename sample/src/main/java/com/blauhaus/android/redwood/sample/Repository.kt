package com.blauhaus.android.redwood.sample

import androidx.lifecycle.MutableLiveData

class Repository {
    val meditationData = MutableLiveData<List<Pair<Float, String>>>()

    init {
        loadData()
    }

    fun loadData() {
        meditationData.postValue(meditationHistoryData)
    }
}