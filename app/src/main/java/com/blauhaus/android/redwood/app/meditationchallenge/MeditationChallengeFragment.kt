package com.blauhaus.android.redwood.app.meditationchallenge.components.meditationchallenge


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.app.R
import kotlinx.android.synthetic.main.meditation_fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


//TODO Refactor: Do I want to rename this App fragment?
class MeditationChallengeFragment : Fragment() {

    private val viewModel by viewModel<MeditationChallengeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.meditation_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentMeditationSession.observe(this, Observer {
            content->
                media_card_todays_session.image = content.image
                media_card_todays_session.preTitle = content.pretitle
                media_card_todays_session.title = content.title
                media_card_todays_session.description = content.description

            if(content.isPlayable()) {
                    media_card_todays_session.onPlay =
                        onPlayCurrentMeditationSession(content.audioId, content.videoId)
                }
        })

        viewModel.updates.observe (this, Observer {
            content->
                media_card_updates.image = content.image
                media_card_updates.preTitle = content.pretitle
                media_card_updates.title = content.title
                media_card_updates.description = content.description
        })

    }

    fun onPlayCurrentMeditationSession(audioId: Int?, videoId:String?): ()->Unit {
        return fun() {

        }
    }

}
