package com.blauhaus.android.redwood

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.features.calculator.CalculatorFragment
import com.blauhaus.android.redwood.features.calculator.CalculatorViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.LastFourWeeksViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.views.DotView
import kotlinx.android.synthetic.main.activity_main.*
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

        lastFourWeeksViewModel.dayData.postValue(demoLastFourWeeksData)
    }

}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null, var refreshing: Boolean = false) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)
}


val demoLastFourWeeksData = listOf(
    DotView.DotViewState.Skipped(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Skipped(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Skipped(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Skipped(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Skipped(),
    DotView.DotViewState.Skipped(),
    DotView.DotViewState.Skipped(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.Met(),
    DotView.DotViewState.MetToday()
)



