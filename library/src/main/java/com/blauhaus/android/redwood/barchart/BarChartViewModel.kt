package com.blauhaus.android.redwood.barchart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BarChartViewModel : ViewModel() {
    val dayData = MutableLiveData<List<Pair<Float, String>>>()
}