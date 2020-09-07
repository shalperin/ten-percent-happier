package com.blauhaus.android.redwood.components.abstractcalendar

import org.koin.dsl.module


val abstractCalendarModule = module {
    single { AbstractCalendarViewModel() }
    factory { AbstractCalendarFragment() }
}
