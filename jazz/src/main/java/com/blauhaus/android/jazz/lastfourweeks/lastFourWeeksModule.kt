package com.blauhaus.android.jazz.lastfourweeks

import org.koin.dsl.module


val lastFourWeeksModule = module {
    single { LastFourWeeksViewModel() }
    factory { LastFourWeeksFragment() }
}
