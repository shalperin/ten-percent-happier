package com.blauhaus.android.redwood.sample.components.meditationdemo.mediacard


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blauhaus.android.redwood.sample.R
import kotlinx.android.synthetic.main.fragment_mediacard.*


/**
 * A simple [Fragment] subclass.
 */
class MediaCard : Fragment() {

    var pretitle: String? = null
    set(value) {
        field = value
        _pretitle.text = value
    }

    var title : String? = null
    set(value) {
        field = value
        _title.text = value
    }

    var description: String? = null
    set(value) {
        field = value
        _description.text = value
    }

    var playButton = true
    set(value) {
        field = value
        if (value) {
            _playButton.visibility = View.VISIBLE
        } else {
            _playButton.visibility = View.GONE
        }
    }

    var playButtonCallback = {_:View -> TODO()}
    set(value) {
        _playButton.setOnClickListener(value)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mediacard, container, false)
    }




}
