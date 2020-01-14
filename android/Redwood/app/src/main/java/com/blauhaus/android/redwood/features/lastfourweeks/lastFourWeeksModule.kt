package com.blauhaus.android.redwood.features.lastfourweeks

import com.blauhaus.android.redwood.features.calculator.*
import org.koin.dsl.module


val lastFourWeeksModule = module {
    single { LastFourWeeksViewModel() }
    factory { LastFourWeeksFragment() }
}
