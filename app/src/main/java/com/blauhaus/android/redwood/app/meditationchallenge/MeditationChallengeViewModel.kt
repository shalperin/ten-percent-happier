package com.blauhaus.android.redwood.app.meditationchallenge.components.meditationchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blauhaus.android.redwood.app.meditationchallenge.data.IRepository
import com.blauhaus.android.redwood.app.meditationchallenge.data.Content

class MeditationChallengeViewModel(repo: IRepository): ViewModel() {
    val currentMeditationSession: MutableLiveData<Content> = repo.currentSession()
    val updates:MutableLiveData<Content> = repo.updates()
}