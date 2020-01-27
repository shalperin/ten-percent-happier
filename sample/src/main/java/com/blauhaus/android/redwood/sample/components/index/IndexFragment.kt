package com.blauhaus.android.redwood.sample.components.index

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.blauhaus.android.redwood.sample.R
import kotlinx.android.synthetic.main.fragment_index.*

class IndexFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_index, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set up our menu links
        link_meditationChallenge.setOnClickListener{
            findNavController().navigate(R.id.action_indexFragment_to_meditationDemoFragment)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            IndexFragment()
    }
}
