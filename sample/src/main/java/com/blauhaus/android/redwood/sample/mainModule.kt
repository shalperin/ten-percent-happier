package com.blauhaus.android.redwood.sample

import com.blauhaus.android.redwood.sample.components.meditationchallenge.MeditationChallengeViewModel
import com.blauhaus.android.redwood.sample.data.IRepository
import com.blauhaus.android.redwood.sample.data.Repository
import com.blauhaus.android.redwood.sample.components.meditationchallenge.statspager.StatsViewModel
import com.blauhaus.android.redwood.mediacard.MediaCardViewModel
import org.koin.dsl.module


val mainModule = module {
    single { Repository() as IRepository }
    single { StatsViewModel(get()) }
    single { MeditationChallengeViewModel(get()) }
    single {MediaCardViewModel()}
}
