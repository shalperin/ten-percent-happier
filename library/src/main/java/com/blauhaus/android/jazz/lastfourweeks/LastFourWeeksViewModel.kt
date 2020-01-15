package com.blauhaus.android.jazz.lastfourweeks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blauhaus.android.jazz.lastfourweeks.views.DayView

class LastFourWeeksViewModel : ViewModel() {
    var dayData = MutableLiveData<List<DayView.ViewState>>()  //nominally of length 28 (4 weeks)
}
