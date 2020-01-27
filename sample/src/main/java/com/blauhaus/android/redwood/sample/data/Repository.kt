package com.blauhaus.android.redwood.sample.data

import androidx.lifecycle.MutableLiveData

class Repository : IRepository {
    val _meditationData = MutableLiveData<List<Pair<Float, String>>>()
    val _globalStats = MutableLiveData<List<Int>>()
    val _myCircle = MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>>()
    val _currentSession =MutableLiveData<Session>()
    val _updates = MutableLiveData<Session>()

    override fun meditationData(): MutableLiveData<List<Pair<Float, String>>> {
        return _meditationData
    }

    override fun globalStats() : MutableLiveData<List<Int>> { return _globalStats }

    override fun myCircle(): MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>> {
        return _myCircle
    }
    override fun currentSession(): MutableLiveData<Session>{ return _currentSession}
    override fun updates(): MutableLiveData<Session>{ return _updates}


    init { loadData() }

    fun loadData() {
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
    fun currentSession(): MutableLiveData<Session>
    fun updates(): MutableLiveData<Session>
}