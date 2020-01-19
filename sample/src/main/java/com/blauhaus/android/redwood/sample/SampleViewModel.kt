package com.blauhaus.android.redwood.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SampleViewModel : ViewModel() {
    val barChartData = MutableLiveData<List<Pair<Float, String>>>()
}