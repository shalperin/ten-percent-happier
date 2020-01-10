package com.blauhaus.android.redwood.features.calculator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.R
import com.blauhaus.android.redwood.Resource
import com.blauhaus.android.redwood.extensions.parseFloat
import kotlinx.android.synthetic.main.calculator_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : Fragment() {
    val model: CalculatorViewModel by viewModel()

    companion object {
        fun newInstance() = CalculatorFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calculator_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_add.setOnClickListener(performAddition)

        model.result.observe(this, Observer { result ->
            field_result.text = result.toString()
        })
        model.errorMessage.observe(this, Observer { msg ->
            Toast.makeText(activity, "error: " + msg, Toast.LENGTH_LONG).show()
        })
        model.loading.observe(this, Observer { loading ->
            if (loading) {
                loading_notification.visibility = View.VISIBLE
            } else {
                loading_notification.visibility = View.GONE
            }
        })
    }

    val performAddition = { view: View ->
        var ra = field_a.parseFloat()
        var rb = field_b.parseFloat()
        if (ra is Resource.Error) {
            model.errorMessage.postValue(ra.message)
        } else if (rb is Resource.Error) {
            model.errorMessage.postValue(rb.message)
        } else {
            model.sum(ra.data ?: 0f, rb.data ?: 0f)
        }
    }

    fun shareModel() : CalculatorViewModel {
        return model
    }

}