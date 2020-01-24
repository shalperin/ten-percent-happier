package com.blauhaus.android.redwood.sample

import com.blauhaus.android.redwood.sample.viewmodels.AchievmentViewModel
import org.koin.dsl.module


val mainModule = module {
    single { Repository() }
    single { AchievmentViewModel(get()) }
}
