package com.blauhaus.android.redwood.sample.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blauhaus.android.redwood.sample.R

/**
 * A simple [Fragment] subclass.
 */
class MeditationDemoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meditation_demo, container, false)
    }


}
