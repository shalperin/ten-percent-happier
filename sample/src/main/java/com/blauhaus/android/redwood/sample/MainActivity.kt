package com.blauhaus.android.redwood.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.blauhaus.android.redwood.barchart.BarChartViewModel
import com.blauhaus.android.redwood.barchart.barChartDemoData
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
        GlobalScope.launch {
            doBarChartDemo(barChartDemoData, barChartViewModel.dayData)
        }
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


private suspend fun doBarChartDemo(startData: List<Pair<Float, String>>, stream: MutableLiveData<List<Pair<Float, String>>>) {

    for (i in 1..4) {
        stream.postValue(startData)
        delay(3600)
        stream.postValue(startData.subList(2, 10))
        delay(3600)
        stream.postValue(startData.subList(1, 5))
        delay(3600)
        stream.postValue(startData.subList(2, 13))
    }
}





