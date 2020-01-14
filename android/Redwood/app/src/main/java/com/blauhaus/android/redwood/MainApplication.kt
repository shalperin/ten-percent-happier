package com.blauhaus.android.redwood

import android.app.Application
import com.blauhaus.android.redwood.features.calculator.CalculatorFragment
import com.blauhaus.android.redwood.features.calculator.calculatorModule
import com.blauhaus.android.redwood.features.lastfourweeks.lastFourWeeksModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(calculatorModule, lastFourWeeksModule))
        }
    }


}