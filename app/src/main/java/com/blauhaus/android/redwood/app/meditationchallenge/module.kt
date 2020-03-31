package com.blauhaus.android.redwood.app.meditationchallenge

import com.blauhaus.android.redwood.app.meditationchallenge.IConfig
import com.blauhaus.android.redwood.app.meditationchallenge.MeditationConfig
import com.blauhaus.android.redwood.app.meditationchallenge.components.meditationchallenge.MeditationChallengeViewModel
import com.blauhaus.android.redwood.app.meditationchallenge.data.IRepository
import com.blauhaus.android.redwood.app.meditationchallenge.data.Repository
import com.blauhaus.android.redwood.app.meditationchallenge.components.statspager.StatsViewModel
import com.blauhaus.android.redwood.app.meditationchallenge.components.mycircle.MyCircleViewModel
import org.koin.dsl.module


val meditationModule = module {
    single { MeditationConfig() as IConfig }
    single { Repository() as IRepository }
    single { StatsViewModel(get(), get()) }
    single { MyCircleViewModel(get(), get())}
    single { MeditationChallengeViewModel( get() ) }
}
