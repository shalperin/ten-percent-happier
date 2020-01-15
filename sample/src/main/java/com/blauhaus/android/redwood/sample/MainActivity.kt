package com.blauhaus.android.redwood.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.blauhaus.android.jazz.lastfourweeks.LastFourWeeksViewModel
import com.blauhaus.android.jazz.lastfourweeks.demoLastFourWeeksData
import com.blauhaus.android.jazz.lastfourweeks.views.DayView
import com.blauhaus.android.redwood.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    val lastFourWeeksViewModel by viewModel<LastFourWeeksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            doLastFourWeeksDemo(
                demoLastFourWeeksData,
                lastFourWeeksViewModel.dayData
            )
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




