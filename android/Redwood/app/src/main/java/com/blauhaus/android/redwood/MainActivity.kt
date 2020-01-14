package com.blauhaus.android.redwood

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.features.calculator.CalculatorFragment
import com.blauhaus.android.redwood.features.calculator.CalculatorViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.LastFourWeeksViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.lastFourWeeksModule
import com.blauhaus.android.redwood.features.lastfourweeks.views.DotView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


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

private suspend fun doLastFourWeeksDemo(startData: MutableList<DotView.DotViewState>, stream: MutableLiveData<List<DotView.DotViewState>>) {
    stream.postValue(startData)
    delay(3600)
    startData[startData.size -1 ]= DotView.DotViewState.MetToday()
    stream.postValue(startData)
}




