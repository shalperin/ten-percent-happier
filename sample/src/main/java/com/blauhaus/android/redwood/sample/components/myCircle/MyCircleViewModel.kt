package com.blauhaus.android.redwood.sample.components.mycircle

import androidx.lifecycle.*
import com.blauhaus.android.redwood.sample.IConfig
import com.blauhaus.android.redwood.sample.data.IRepository

class MyCircleViewModel(private val repo: IRepository, private val config: IConfig) : ViewModel() {


    //TODO Refactor: This data structure is horrible.
    private val _myCircleBackingData = MediatorLiveData<List<Pair<String, List<Pair<Float, String>>>>>()
    private fun combineMediationData(
        me: LiveData<List<Pair<Float, String>>>,
        friends: LiveData<List<Pair<String, List<Pair<Float, String>>>>>,
        myUsername: LiveData<String>)
    {

        val myData = me.value
        val listOfFriendsData = friends.value
        val myName  = myUsername.value

        if (myData == null || listOfFriendsData == null || myName == null) { return}

        _myCircleBackingData.value = listOf(Pair(myName, myData)).union(listOfFriendsData).toList()

    }



    // I'm conflicted about the 'it' masking below... I'm not sure that logically naming things
    // is going to make this any more readable.
    //TODO needs test.
    private val _myCircle:LiveData<List<Transport>> = Transformations.map(_myCircleBackingData){
        it.map{
            val name:String = it.first
            val days:Int = it.second.filter{
                it.first > 0
            }.size
            val avg:Int = it.second.map{it.first}.average().toInt()
            val didCompleteChallenge = days > config.achievementThresholdInDays()

            //geeze paranoid of truncation or what?
            val progress = (days.toFloat() / config.achievementThresholdInDays().toFloat() * 100.00).toInt()


            Transport(
                name,
                days,
                avg,
                didCompleteChallenge,
                progress
            )
        }
    }

    //TODO refactor: Is this a model?  Get rid of the generic 'transport' name.
    data class Transport(val fname:String, val days:Int, val avg:Int,
                         val didCompleteChallenge:Boolean, val progress:Int)

    fun myCircle():LiveData<List<Transport>> { return _myCircle }


    init{
        _myCircleBackingData.addSource(repo.meditationData()){
            combineMediationData(repo.meditationData(), repo.myCircle(), repo.userName())
        }
        _myCircleBackingData.addSource(repo.myCircle()) {
            combineMediationData(repo.meditationData(), repo.myCircle(), repo.userName())
        }
        _myCircleBackingData.addSource(repo.userName()) {
            combineMediationData(repo.meditationData(), repo.myCircle(), repo.userName())
        }
    }

}