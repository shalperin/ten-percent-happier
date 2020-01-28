package com.blauhaus.android.redwood.sample

import com.blauhaus.android.redwood.sample.components.meditationchallenge.MeditationChallengeViewModel
import com.blauhaus.android.redwood.sample.data.IRepository
import com.blauhaus.android.redwood.sample.data.Repository
import com.blauhaus.android.redwood.sample.components.meditationchallenge.achievementpager.AchievementViewModel
import com.blauhaus.android.redwood.sample.components.meditationchallenge.mediacard.MediaCardViewModel
import org.koin.dsl.module


val mainModule = module {
    single { Repository() as IRepository }
    single { AchievementViewModel(get()) }
    single { MeditationChallengeViewModel(get()) }
    single {MediaCardViewModel()}
}
