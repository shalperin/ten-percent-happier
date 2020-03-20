package com.blauhaus.android.redwood.sample.data

import androidx.lifecycle.MutableLiveData
import com.blauhaus.android.redwood.sample.data.models.Content

class Repository : IRepository {

    // TODO refactor.
    // This should be an object with named fields.  What is the Float? What is the String?
    private val _meditationData = MutableLiveData<List<Pair<Float, String>>>()
    //TODO Same Comment
    private val _globalStats = MutableLiveData<List<Int>>()
    // TODO Same comment.
    private val _myCircle = MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>>()
    private val _currentSession =MutableLiveData<Content>()
    private val _updates = MutableLiveData<Content>()
    // See this is OK, generic, because it's obvious.
    val _username =  MutableLiveData<String>()

    // TODO see above
    override fun meditationData(): MutableLiveData<List<Pair<Float, String>>> {
        return _meditationData
    }

    override fun globalStats() : MutableLiveData<List<Int>> { return _globalStats }

    override fun myCircle(): MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>> {
        return _myCircle
    }
    override fun currentSession(): MutableLiveData<Content>{ return _currentSession}
    override fun updates(): MutableLiveData<Content>{ return _updates}

    override fun userName() :MutableLiveData<String> {return _username}

    init { loadData() }

    private fun loadData() {
        _meditationData.postValue(meditationHistoryData)
        _globalStats.postValue(globalStatsData)
        _myCircle.postValue(myCircleData)
        _currentSession.postValue(currentSessionData)
        _updates.postValue(challengeUpdateData)
        _username.postValue(demoUserName)
    }


    //TODO refactor this into a data class.
    companion object {
        val GLOBAL_DATA_PARTICIPANTS_IDX = 0
        val GLOBAL_DATA_MINUTES_IDX = 1
        val GLOBAL_DATA_AVERAGE_IDX = 2
    }
}

interface IRepository {
    fun meditationData(): MutableLiveData<List<Pair<Float, String>>>
    fun globalStats(): MutableLiveData<List<Int>>
    fun myCircle(): MutableLiveData<List<Pair<String, List<Pair<Float, String>>>>>
    fun currentSession(): MutableLiveData<Content>
    fun updates(): MutableLiveData<Content>
    fun userName() :MutableLiveData<String>
}