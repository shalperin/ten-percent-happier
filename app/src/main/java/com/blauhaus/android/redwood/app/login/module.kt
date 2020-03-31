package com.blauhaus.android.redwood.app.login

import org.koin.dsl.module


val loginModule = module {
    single {LoginViewModel()}
}