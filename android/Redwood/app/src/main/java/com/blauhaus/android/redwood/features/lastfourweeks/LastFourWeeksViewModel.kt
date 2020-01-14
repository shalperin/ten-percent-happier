package com.blauhaus.android.redwood.features.lastfourweeks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blauhaus.android.redwood.features.lastfourweeks.views.DotView

class LastFourWeeksViewModel : ViewModel() {
    var dayData = MutableLiveData<List<DotView.DotViewState>>()
}
