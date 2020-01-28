package com.blauhaus.android.redwood.sample.components.meditationchallenge.statspager

import androidx.lifecycle.*
import com.blauhaus.android.redwood.lastfourweeks.views.DayView
import com.blauhaus.android.redwood.sample.*
import com.blauhaus.android.redwood.sample.data.IRepository
import com.blauhaus.android.redwood.sample.data.Repository.Companion.GLOBAL_DATA_AVERAGE_IDX
import com.blauhaus.android.redwood.sample.data.Repository.Companion.GLOBAL_DATA_MINUTES_IDX
import com.blauhaus.android.redwood.sample.data.Repository.Companion.GLOBAL_DATA_PARTICIPANTS_IDX

class StatsViewModel(val repo: IRepository, val config:IConfig): ViewModel() {

    val lastFourWeeksBackingModel: LiveData<List<DayView.ViewState>> =
        Transformations.map(repo.meditationData()) { data ->
            val adapted = data.map { datum ->
                if (datum.first == 0f) {
                    DayView.ViewState.Skipped()
                } else {
                    DayView.ViewState.Met()
                }
            }.toMutableList()
            adapted.add(DayView.ViewState.DidntMeetYetToday())
            while (adapted.size <= 28) {
                adapted.add(DayView.ViewState.Future())
            }
            adapted
        }

    val barChartBackingModel = repo.meditationData()

    val totalDaysMeditated: LiveData<Int> =
        Transformations.map(lastFourWeeksBackingModel) {
            it.filterIsInstance<DayView.ViewState.Met>().size -1
        }

    val averageMinutesMeditated: LiveData<Int> =
        Transformations.map(repo.meditationData()) {
            it.map{it.first}.filter{it != 0f}.average().toInt()
        }


    val medalClass = MediatorLiveData<Pair<MedalClass, MedalProgress>>()
    private fun combineMetalClassData(
        daysMeditated: LiveData<Int>,
        avgMinutesMeditated: LiveData<Int>
    ){

        val days:Int? = daysMeditated.value
        val avg:Int? = avgMinutesMeditated.value

        if (days == null || avg == null) {
            medalClass.value = Pair(
                MedalClass.None(),
                MedalProgress.No()
            )
            return
        }

        val _medalClass = if (avg >= config.GOLD_MEDAL_THRESHOLD_IN_MINUTES()) {
            MedalClass.Gold()
        } else if (avg >= config.SILVER_MEDAL_THRESHOLD_IN_MINUTES()) {
            MedalClass.Silver()
        } else if (avg >= config.BRONZE_MEDAL_THRESHOLD_IN_MINUTES()) {
            MedalClass.Bronze()
        } else {
            MedalClass.None()
        }

        val medalProgress = if (days >= config.TOTAL_DAYS_ACHIEVEMENT_THRESHOLD()) {
            MedalProgress.Got()
        } else if (_medalClass is MedalClass.None) {
            MedalProgress.No()
        } else {
            MedalProgress.OnTrack()
        }

        medalClass.value = Pair(_medalClass, medalProgress)
    }

    //TODO get rid of these indecies and replace with data class.
    val globalParticipants = Transformations.map(repo.globalStats()){ it[GLOBAL_DATA_PARTICIPANTS_IDX] }
    val globalMinutes = Transformations.map(repo.globalStats()){ it[GLOBAL_DATA_MINUTES_IDX] }
    val globalAverage = Transformations.map(repo.globalStats()){ it[GLOBAL_DATA_AVERAGE_IDX] }


    init {
        medalClass.addSource(totalDaysMeditated) { combineMetalClassData(totalDaysMeditated, averageMinutesMeditated) }
        medalClass.addSource(averageMinutesMeditated) { combineMetalClassData(totalDaysMeditated, averageMinutesMeditated) }
    }

    sealed class MedalClass() {
        class Gold(): MedalClass()
        class Silver(): MedalClass()
        class Bronze(): MedalClass()
        class None(): MedalClass()
    }

    sealed class MedalProgress() {
         class Got() : MedalProgress()
         class OnTrack(): MedalProgress()
        class No() : MedalProgress()
    }
}
