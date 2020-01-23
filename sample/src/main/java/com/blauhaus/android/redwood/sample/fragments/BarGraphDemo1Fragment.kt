package com.blauhaus.android.redwood.sample.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.blauhaus.android.redwood.barchart.BarChartFragment
import com.blauhaus.android.redwood.barchart.BarChartViewModel
import com.blauhaus.android.redwood.barchart.PROP_BAR_COLOR
import com.blauhaus.android.redwood.barchart.barChartDemoData
import com.blauhaus.android.redwood.sample.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */

private const val CHART_ID_1 = "chart_1"
private const val CHART_ID_2 = "chart_2"
private const val CHART_ID_3 = "chart_3"

class BarGraphDemo1Fragment : Fragment() {
    private val chartModel by viewModel<BarChartViewModel>()
    private val chart1 = BarChartFragment.newInstance(CHART_ID_1)
    private val chart2 = BarChartFragment.newInstance(CHART_ID_2)
    private val chart3 = BarChartFragment.newInstance(CHART_ID_3)
    private var animation: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_graph_demo1, container, false)
    }

    //TODO() get some feedback on how we've built and torn down the child fragments.
    override fun onStart() {
        super.onStart()
                fragmentManager?.
                    beginTransaction()?.
                    add(R.id.bar_chart_container_1, chart1 )?.
                    add(R.id.bar_chart_container_2, chart2)?.
                    add(R.id.bar_chart_container_3, chart3)?.
                    commit()

        chartModel.setIntProp(
            CHART_ID_2,
            PROP_BAR_COLOR,
            ContextCompat.getColor(context!!,
                R.color.barChart2BarColor
            )
        )
        chartModel.setIntProp(
            CHART_ID_3,
            PROP_BAR_COLOR,
            ContextCompat.getColor(context!!,
                R.color.barChart3BarColor
            )
        )

        animation = GlobalScope.launch { loadData() }

    }

    override fun onStop() {
        super.onStop()
        animation?.cancel()
        fragmentManager?.beginTransaction()?.remove(chart1)?.remove(chart2)?.remove(chart3)
    }



    private suspend fun loadData() {
        chartModel.setData(CHART_ID_1, barChartDemoData.shuffled())
        chartModel.setData(CHART_ID_2, barChartDemoData.subList(2, 9).shuffled())
        chartModel.setData(CHART_ID_3, (barChartDemoData + barChartDemoData).shuffled())

        for(i in 0..500) {
            delay(150)
            chartModel.setData(
                CHART_ID_3,
                (barChartDemoData + barChartDemoData).shuffled()
            )

        }
    }

}
