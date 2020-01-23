package com.blauhaus.android.redwood.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blauhaus.android.redwood.barchart.PROP_BAR_COLOR
import com.blauhaus.android.redwood.barchart.BarChartFragment
import com.blauhaus.android.redwood.barchart.BarChartViewModel
import com.blauhaus.android.redwood.barchart.barChartDemoData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

//  val lastFourWeeksViewModel by viewModel<LastFourWeeksViewModel>()
//
//        GlobalScope.launch {
//            lastFourWeeksViewModel.dayData.postValue(demoLastFourWeeksData)
//            delay(3600)
//
//            // replace the last element in the list with MetToday()
//            val next = demoLastFourWeeksData.mapIndexed{i, existing ->
//                if (i == demoLastFourWeeksData.size-1)
//                    DayView.ViewState.MetToday() else existing}
//
//            lastFourWeeksViewModel.dayData.postValue(next)
//        }
