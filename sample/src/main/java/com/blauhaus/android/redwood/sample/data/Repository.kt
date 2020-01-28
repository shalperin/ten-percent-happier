package com.blauhaus.android.redwood.sample.data

import androidx.lifecycle.MutableLiveData

class Repository : IRepository {
    private val _meditationData = MutableLiveData<List<Pair<Float, String>>>()
    private val _globalStats = MutableLiveData<List<Int>>()
    private val _myCircle = MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>>()
    private val _currentSession =MutableLiveData<Content>()
    private val _updates = MutableLiveData<Content>()

    override fun meditationData(): MutableLiveData<List<Pair<Float, String>>> {
        return _meditationData
    }

    override fun globalStats() : MutableLiveData<List<Int>> { return _globalStats }

    override fun myCircle(): MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>> {
        return _myCircle
    }
    override fun currentSession(): MutableLiveData<Content>{ return _currentSession}
    override fun updates(): MutableLiveData<Content>{ return _updates}


    init { loadData() }

    private fun loadData() {
        _meditationData.postValue(meditationHistoryData)
        _globalStats.postValue(globalStatsData)
        _myCircle.postValue(myCircleData)
        _currentSession.postValue(currentSessionData)
        _updates.postValue(challengeUpdateData)
    }

    //TODO refactor this into a data class.
    companion object {
        val GLOBAL_DATA_PARTICIPANTS = 0
        val GLOBAL_DATA_MINUTES = 1
        val GLOBAL_DATA_AVERAGE = 2
    }
}

interface IRepository {
    fun meditationData(): MutableLiveData<List<Pair<Float, String>>>
    fun globalStats(): MutableLiveData<List<Int>>
    fun myCircle(): MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>>
    fun currentSession(): MutableLiveData<Content>
    fun updates(): MutableLiveData<Content>
}