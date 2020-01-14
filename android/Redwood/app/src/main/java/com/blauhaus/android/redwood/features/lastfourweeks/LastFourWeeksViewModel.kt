package com.blauhaus.android.redwood.features.lastfourweeks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.views.DayView

class LastFourWeeksViewModel : ViewModel() {
    var dayData = MutableLiveData<List<DayView.ViewState>>()  //nominally of length 28 (4 weeks)
}
