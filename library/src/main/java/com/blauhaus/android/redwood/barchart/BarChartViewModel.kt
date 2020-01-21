package com.blauhaus.android.redwood.barchart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BarChartViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var data = MutableLiveData<List<Pair<Float,String>>>()
    var _activeItem = MutableLiveData<Int?>()
}
