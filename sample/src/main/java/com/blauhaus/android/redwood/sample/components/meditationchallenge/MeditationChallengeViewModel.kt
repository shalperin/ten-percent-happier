package com.blauhaus.android.redwood.sample.components.meditationchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blauhaus.android.redwood.sample.data.IRepository
import com.blauhaus.android.redwood.sample.data.models.Content

class MeditationChallengeViewModel(repo: IRepository): ViewModel() {
    val currentMeditationSession: MutableLiveData<Content> = repo.currentSession()
    val updates:MutableLiveData<Content> = repo.updates()
}