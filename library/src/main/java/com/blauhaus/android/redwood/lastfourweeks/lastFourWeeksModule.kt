package com.blauhaus.android.redwood.lastfourweeks

import org.koin.dsl.module


val lastFourWeeksModule = module {
    single { LastFourWeeksViewModel() }
    factory { LastFourWeeksFragment() }
}
