package com.blauhaus.android.redwood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.blauhaus.android.redwood.features.calculator.CalculatorViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.LastFourWeeksViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.views.DayView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


/*
IDEAS:
    + KOIN injection (repo)
    + Testing
 */


class MainActivity : AppCompatActivity() {
    val tag="SQH_MAIN_ACTIVITY"
    val calculatorViewModel by viewModel<CalculatorViewModel>()


    val lastFourWeeksViewModel by viewModel<LastFourWeeksViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            doLastFourWeeksDemo(demoLastFourWeeksData, lastFourWeeksViewModel.dayData)
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




