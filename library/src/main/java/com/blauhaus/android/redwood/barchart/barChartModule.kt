package com.blauhaus.android.redwood.barchart

import org.koin.dsl.module

val barChartModule = module {
    single { BarChartViewModel()}
    factory {BarChartFragment() }
}