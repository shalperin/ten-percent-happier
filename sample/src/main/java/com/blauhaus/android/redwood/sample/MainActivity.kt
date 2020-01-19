package com.blauhaus.android.redwood.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blauhaus.android.redwood.barchart.barChartDemoData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model = ViewModelProviders.of(this)[SampleViewModel::class.java]

        model.barChartData.observe(this, Observer {
            bar_chart.model = it
        })

        GlobalScope.launch {
            for (i in 1..10) {
                model.barChartData.postValue(barChartDemoData)
                delay(5600)
                model.barChartData.postValue(barChartDemoData.subList(2, 10))
                delay(5600)
                model.barChartData.postValue(barChartDemoData.subList(1, 5))
                delay(5600)
                model.barChartData.postValue(barChartDemoData.subList(2, 13))
                delay(5600)
            }
        }
    }
}

//
//private suspend fun doLastFourWeeksDemo(startData: List<DayView.ViewState>, stream: MutableLiveData<List<DayView.ViewState>>) {
//    stream.postValue(startData)
//    delay(3600)
//
//    // replace the last element in the list with MetToday()
//    val next = startData.mapIndexed{i, existing ->
//        if (i == startData.size-1) DayView.ViewState.MetToday() else existing}
//
//    stream.postValue(next)
//}



