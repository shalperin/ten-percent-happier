package com.blauhaus.android.redwood.sample

import android.app.Application
import com.blauhaus.android.jazz.lastfourweeks.lastFourWeeksModule
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
            modules(listOf(lastFourWeeksModule))
        }
    }


}