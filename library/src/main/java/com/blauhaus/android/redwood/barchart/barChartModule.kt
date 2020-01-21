package com.blauhaus.android.redwood.barchart

import com.blauhaus.android.redwood.lastfourweeks.LastFourWeeksFragment
import org.koin.dsl.module


val barChartModule = module {
    single { BarChartViewModel() }
    factory { BarChartFragment() }
}
