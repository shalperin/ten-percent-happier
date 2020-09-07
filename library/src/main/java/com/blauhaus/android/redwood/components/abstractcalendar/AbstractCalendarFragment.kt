package com.blauhaus.android.redwood.components.abstractcalendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.R
import kotlinx.android.synthetic.main.fragment_last_four_weeks.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AbstractCalendarFragment : Fragment() {
    private val model: AbstractCalendarViewModel by viewModel()

    lateinit var views: List<DayView>

    companion object {
        fun newInstance() = AbstractCalendarFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_last_four_weeks, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views = listOf(day0, day1, day2, day3, day4, day5, day6, day7, day8, day9, day10, day11,
            day12, day13, day14, day15, day16, day17, day18, day19, day20, day21, day22, day23, day24,
            day25, day26, day27)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //TODO this code belongs in OnViewCreated, onActivityCreated is deprecated.
        //Should also use viewLifecycleOwner...
        model.dayData.observe(this, Observer { states ->
            views.forEach{it.show(DayView.ViewState.Future())}
            views.zip(states).forEach{ (day, state) ->
                day.show(state)
            }
        })
    }
}
