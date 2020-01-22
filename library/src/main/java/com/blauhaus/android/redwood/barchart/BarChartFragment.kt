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

private const val ID_ARG_PARAM = "id_param"

class BarChartFragment : Fragment(), BarChartFragmentListener {
    private val model: BarChartViewModel by viewModel()
    private var id: String? = null

    companion object {
        fun newInstance(id: String) = BarChartFragment().apply {
            arguments = Bundle().apply {
                putString(ID_ARG_PARAM, id)
            }
        }

        const val BAR_COLOR_PROP = "barColor"
        const val GRID_LINE_COLOR_PROP = "gridLineColor"
        const val LABEL_COLOR_PROP = "labelColor"
        const val TEXT_COLOR_PROP = "textColor"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID_ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bar_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bar_chart_view.listener = this

        model.data(id!!).observe(this, Observer {
            bar_chart_view.model = it
        })

        model.activeItem(id!!).observe(this, Observer {index ->
            bar_chart_view.labelIndex = index
        })

        model.intProp(id!!, BAR_COLOR_PROP).observe(this, Observer {
            //TODO : why is 'it' type Int!?
            bar_chart_view.barColor = it
        })
        model.intProp(id!!, GRID_LINE_COLOR_PROP).observe(this, Observer {
            bar_chart_view.gridLineColor = it
        })
        model.intProp(id!!, LABEL_COLOR_PROP).observe(this, Observer {
            bar_chart_view.labelColor = it
        })
        model.intProp(id!!, TEXT_COLOR_PROP).observe(this, Observer {
            bar_chart_view.textColor = it
        })

    }

    override fun onActiveItemSet(index: Int?) {
        model.activeItem(id!!).postValue(index)
    }

}

interface BarChartFragmentListener {
    fun onActiveItemSet(index:Int?)
}
