package com.blauhaus.android.redwood.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blauhaus.android.redwood.barchart.BarChartViewModel
import com.blauhaus.android.redwood.barchart.barChartDemoData
import com.blauhaus.android.redwood.lastfourweeks.LastFourWeeksViewModel
import com.blauhaus.android.redwood.lastfourweeks.demoLastFourWeeksData
import com.blauhaus.android.redwood.lastfourweeks.views.DayView
import kotlinx.android.synthetic.main.activity_main.*
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

        go.setOnClickListener {
            GlobalScope.launch {
                for (i in 1..10) {
                    barChartViewModel.data.postValue(barChartDemoData)
//                delay(5600)
//                barChartViewModel.data.postValue(barChartDemoData.subList(2, 10))
//                delay(5600)
//                barChartViewModel.data.postValue(barChartDemoData.subList(1, 5))
//                delay(5600)
//                barChartViewModel.data.postValue(barChartDemoData.subList(2, 13))
//                delay(5600)
                }
            }
        }

        GlobalScope.launch {
            lastFourWeeksViewModel.dayData.postValue(demoLastFourWeeksData)
            delay(3600)

            // replace the last element in the list with MetToday()
            val next = demoLastFourWeeksData.mapIndexed{i, existing ->
                if (i == demoLastFourWeeksData.size-1)
                    DayView.ViewState.MetToday() else existing}

            lastFourWeeksViewModel.dayData.postValue(next)
        }
    }
}