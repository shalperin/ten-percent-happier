package com.blauhaus.android.redwood.sample.viewmodels

import androidx.lifecycle.*
import com.blauhaus.android.redwood.lastfourweeks.views.DayView
import com.blauhaus.android.redwood.sample.*
import org.koin.android.ext.android.inject

class AchievmentViewModel(val repo: Repository): ViewModel() {



    val lastFourWeeksBackingModel: LiveData<List<DayView.ViewState>> =
        Transformations.map(repo.meditationData) { data ->
            adaptRepoMeditationDataToLastFourWeeksDayData(data)
        }

    val barChartBackingModel: LiveData<List<Pair<Float, String>>> =
        Transformations.map(repo.meditationData) { data ->
            adaptRepoMeditationDataToBarChartData(data)
        }

    val totalDaysMeditated: LiveData<Int> =
        Transformations.map(lastFourWeeksBackingModel) {
            it.filterIsInstance<DayView.ViewState.Met>().size
        }

    val averageMinutesMeditated: LiveData<Int> =
        Transformations.map(repo.meditationData) {
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
            medalClass.value = Pair(MedalClass.None(), MedalProgress.No())
            return
        }

        val _medalClass = if (avg >= GOLD_MEDAL_THRESHOLD_IN_MINUTES) {
            MedalClass.Gold()
        } else if (avg >= SILVER_MEDAL_THRESHOLD_IN_MINUTES) {
            MedalClass.Silver()
        } else if (avg >= BRONZE_MEDAL_THRESHOLD_IN_MINUTES) {
            MedalClass.Bronze()
        } else {
            MedalClass.None()
        }

        val medalProgress = if (days >= TOTAL_DAYS_ACHIEVEMENT_THRESHOLD) {
            MedalProgress.Got()
        } else if (_medalClass is MedalClass.None) {
            MedalProgress.No()
        } else {
            MedalProgress.OnTrack()
        }

        medalClass.value = Pair(_medalClass, medalProgress)
    }



    //TODO move these up
    private fun adaptRepoMeditationDataToLastFourWeeksDayData(data: List<Pair<Float, String>>): MutableList<DayView.ViewState> {
        val adapted = data.map { datum ->
            if (datum.first == 0f) {
                DayView.ViewState.Skipped()
            } else {
                DayView.ViewState.Met()
            }
        }.toMutableList()
        adapted.add(DayView.ViewState.DidntMeetYetToday())
        while (adapted.size != 28) {
            adapted.add(DayView.ViewState.Future())
        }
        return adapted
    }


    private fun adaptRepoMeditationDataToBarChartData(data: List<Pair<Float, String>>): List<Pair<Float, String>> {
        var adapted = data.toMutableList()
        while (adapted.size != 28) {
            adapted.add(Pair(0f, "0 minutes"))
        }
        return adapted.toList()
    }


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
