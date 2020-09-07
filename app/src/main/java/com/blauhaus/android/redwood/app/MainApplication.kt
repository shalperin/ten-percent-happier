package com.blauhaus.android.redwood.app

import android.app.Application
import com.blauhaus.android.redwood.app.common.firebase.firebaseModule
import com.blauhaus.android.redwood.app.login.loginModule
import com.blauhaus.android.redwood.components.barchart.barChartModule
import com.blauhaus.android.redwood.components.abstractcalendar.abstractCalendarModule
import com.blauhaus.android.redwood.app.meditationchallenge.meditationModule
import com.blauhaus.android.redwood.app.todomvvm.todoModule
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)

            // TODO: Refactor
            // I want to get rid of lastFourWeeksModule and barChartModule from here.
            // I want the meditationModule to pull them in.
            // Have to research how to do that.
            modules(listOf(
                abstractCalendarModule,
                barChartModule,
                meditationModule,
                loginModule,
                firebaseModule,
                todoModule
            ))


        }

        JodaTimeAndroid.init(this);

    }


}