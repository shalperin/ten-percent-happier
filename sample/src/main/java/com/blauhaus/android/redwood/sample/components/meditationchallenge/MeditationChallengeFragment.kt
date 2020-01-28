package com.blauhaus.android.redwood.sample.components.meditationchallenge


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.sample.R
import com.blauhaus.android.redwood.mediacard.MediaCardFragment
import com.blauhaus.android.redwood.mediacard.MediaCardViewModel
import com.blauhaus.android.redwood.sample.data.Content
import org.koin.androidx.viewmodel.ext.android.viewModel


class MeditationChallengeFragment : Fragment() {

    private val meditationChallengeViewModel by viewModel<MeditationChallengeViewModel>()
    private val cardModel by viewModel<MediaCardViewModel>()
    private val TODAYFRAGID = 1
    private val UPDATEFRAGID = 2

    // why can't we use this same 'apply' technique (or a later 'set' call) to setup our styling?
    // the viewmodel approach seems pretty heavy handed for something that could
    // also have been implemented as a 'dumb' view.
    private val todaysMediationSessionFragment = MediaCardFragment().apply{fragId = TODAYFRAGID}
    private val updatesFragment = MediaCardFragment().apply{fragId = UPDATEFRAGID}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meditation_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentManager?.beginTransaction()
            ?.add(R.id.todays_session_container, todaysMediationSessionFragment)
            ?.add(R.id.challenge_updates_container, updatesFragment)
            ?.commit()

        meditationChallengeViewModel.currentMeditationSession.observe(this, Observer {
            cardModel.data(TODAYFRAGID).postValue(mediaCardTransportFromContent(it))
        })

        meditationChallengeViewModel.updates.observe (this, Observer {
            cardModel.data(UPDATEFRAGID).postValue(mediaCardTransportFromContent(it))
        })
    }

    //probably move this somewhere more general.
    // if mediaCard is the in the reusable library,
    // then this adapter belongs with our Content class.
    // The specific implementation owns the adapter.
    private fun mediaCardTransportFromContent(session:Content): MediaCardViewModel.Transport {
        when (session) {
            is Content.Meditation -> {
                return MediaCardViewModel.Transport.Playable(
                    session.image,
                    session.title,
                    session.pretitle,
                    session.description,
                    session.audioId,
                    session.videoId
                )
            }
            is Content.Update -> {
                return MediaCardViewModel.Transport.Static(
                    session.image,
                    session.title,
                    session.pretitle,
                    session.description
                )
            }
        }
    }
}
