package com.blauhaus.android.redwood.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.blauhaus.android.redwood.barchart.BarChartViewModel
import com.blauhaus.android.redwood.barchart.demoData
import com.blauhaus.android.redwood.lastfourweeks.LastFourWeeksViewModel
import com.blauhaus.android.redwood.lastfourweeks.demoLastFourWeeksData
import com.blauhaus.android.redwood.lastfourweeks.views.DayView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    val lastFourWeeksViewModel by viewModel<LastFourWeeksViewModel>()
    val barChartViewModel by viewModel<BarChartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //pump some data into the LastFourWeeks feature
        GlobalScope.launch {
            doLastFourWeeksDemo(
                demoLastFourWeeksData,
                lastFourWeeksViewModel.dayData
            )
        }

        //Same thing for the bar chart.
        barChartViewModel.dayData.postValue(demoData)
    }

}

private suspend fun doLastFourWeeksDemo(startData: List<DayView.ViewState>, stream: MutableLiveData<List<DayView.ViewState>>) {
    stream.postValue(startData)
    delay(3600)

    // replace the last element in the list with MetToday()
    val next = startData.mapIndexed{i, existing ->
        if (i == startData.size-1) DayView.ViewState.MetToday() else existing}

    stream.postValue(next)
}




