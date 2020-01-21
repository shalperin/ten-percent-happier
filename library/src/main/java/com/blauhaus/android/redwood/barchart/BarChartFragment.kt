package com.blauhaus.android.redwood.barchart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.blauhaus.android.redwood.R
import kotlinx.android.synthetic.main.bar_chart_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BarChartFragment : Fragment(), BarChartFragmentListener {
    private val model: BarChartViewModel by viewModel()

    companion object {
        fun newInstance() = BarChartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bar_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.data.observe(this, Observer {
            bar_chart_view.model = it
        })
        bar_chart_view.listener = this

        model._activeItem.observe(this, Observer {index ->
            bar_chart_view.labelIndex = index
        })
    }

    override fun onActiveItemSet(index: Int?) {
        model._activeItem.postValue(index)
    }

}

interface BarChartFragmentListener {
    fun onActiveItemSet(index:Int?)
}
