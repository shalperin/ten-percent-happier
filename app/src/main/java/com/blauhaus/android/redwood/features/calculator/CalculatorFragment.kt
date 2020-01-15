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
    private val model: CalculatorViewModel by viewModel()
    private var id:Int? = null
    companion object {
        val ID_KEY = "id"

        fun newInstance(id:Int):CalculatorFragment  {
            var frag = CalculatorFragment()
            val args = Bundle()
            args.putInt(ID_KEY, id)
            frag.arguments = args
            return frag

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = arguments!!.getInt(ID_KEY)
        }
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

        model.result(id!!)?.observe(this, Observer { result ->
            field_result.text = result.toString()
        })
        model.errorMessage(id!!)?.observe(this, Observer { msg ->
            Toast.makeText(activity, "error: " + msg, Toast.LENGTH_LONG).show()
        })
        model.loading(id!!)?.observe(this, Observer { loading ->
            if (loading) {
                loading_notification.visibility = View.VISIBLE
            } else {
                loading_notification.visibility = View.GONE
            }
        })
    }

    val performAddition = object : View.OnClickListener {
        override fun onClick(v: View?) {
            var ra = field_a.parseFloat()
            var rb = field_b.parseFloat()
            if (ra is Resource.Error) {
                model.errorMessage(id!!)?.postValue(ra.message)
            } else if (rb is Resource.Error) {
                model.errorMessage(id!!)?.postValue(rb.message)
            } else {
                model.sum(id!!, ra.data ?: 0f, rb.data ?: 0f)
            }
        }
    }
}