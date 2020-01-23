package com.blauhaus.android.redwood.sample.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blauhaus.android.redwood.lastfourweeks.LastFourWeeksFragment

import com.blauhaus.android.redwood.lastfourweeks.LastFourWeeksViewModel
import com.blauhaus.android.redwood.lastfourweeks.demoLastFourWeeksData
import com.blauhaus.android.redwood.lastfourweeks.views.DayView
import com.blauhaus.android.redwood.sample.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LastFourWeeksDemo1Fragment : Fragment() {
    private val model by viewModel<LastFourWeeksViewModel>()
    private var job: Job? = null
    private val frag = LastFourWeeksFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_last_four_weeks_demo1, container, false)
    }

    override fun onStart() {
        super.onStart()
        fragmentManager?.beginTransaction()?.add(R.id.l4wcontainer1, frag)?.commit()

        job = GlobalScope.launch {
            model.dayData.postValue(demoLastFourWeeksData)
            delay(3600)

            // replace the last element in the list with MetToday()
            val next = demoLastFourWeeksData.mapIndexed{i, existing ->
                if (i == demoLastFourWeeksData.size-1)
                    DayView.ViewState.MetToday() else existing}

            model.dayData.postValue(next)
        }
    }

    override fun onStop() {
        super.onStop()
        fragmentManager?.beginTransaction()?.remove(frag)?.commit()
        job?.cancel()
    }
}
