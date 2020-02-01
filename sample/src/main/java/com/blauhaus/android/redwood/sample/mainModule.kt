package com.blauhaus.android.redwood.sample

import com.blauhaus.android.redwood.sample.components.meditationChallenge.MeditationChallengeViewModel
import com.blauhaus.android.redwood.sample.data.IRepository
import com.blauhaus.android.redwood.sample.data.Repository
import com.blauhaus.android.redwood.sample.components.statsPager.StatsViewModel
import com.blauhaus.android.redwood.sample.components.myCircle.MyCircleViewModel
import org.koin.dsl.module


val mainModule = module {
    single { Config() as IConfig}
    single { Repository() as IRepository }
    single { StatsViewModel(get(), get()) }
    single { MyCircleViewModel(get(), get())}
    single { MeditationChallengeViewModel( get() ) }
}
