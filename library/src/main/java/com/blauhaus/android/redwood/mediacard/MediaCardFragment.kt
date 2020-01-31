package com.blauhaus.android.redwood.mediacard


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.R
import kotlinx.android.synthetic.main.fragment_mediacard.*
import org.koin.androidx.viewmodel.ext.android.viewModel

//?Refactor:  This could be a simple View.
class MediaCardFragment : Fragment() {
    val model by viewModel<MediaCardViewModel>()
    var fragId = 0

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

    var image: Int? = null
    set(value) {
        field = value
        if (field != null) {
            _image.setImageDrawable(ResourcesCompat.getDrawable(resources, field!!,null))
        }
    }


    var audioId: Int? = null
    var videoId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mediacard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.data(fragId).observe(this, Observer { data
            ->
            when(data) {
                is MediaCardViewModel.Transport.Playable -> {
                   image = data.image
                    title = data.title
                    pretitle = data.preTitle
                    description = data.description
                    /////////////
                    playButton = true
                    audioId = data.audioId
                    videoId = data.videoId
                }
                is MediaCardViewModel.Transport.Static -> {
                    image = data.image
                    title = data.title
                    pretitle = data.preTitle
                    description = data.description
                    //////////////
                    playButton = false
                    audioId = null
                    videoId = null
                }
            }

        })
    }
}
