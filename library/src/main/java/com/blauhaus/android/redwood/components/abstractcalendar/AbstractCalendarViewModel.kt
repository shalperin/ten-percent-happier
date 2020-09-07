package com.blauhaus.android.redwood.components.abstractcalendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AbstractCalendarViewModel : ViewModel() {
    var dayData = MutableLiveData<List<DayView.ViewState>>()  //nominally of length 28 (4 weeks)
}
