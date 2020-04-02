package com.blauhaus.android.redwood.app.todomvvm

import org.koin.dsl.module

var todoModule = module {
    single { TodoFirestoreRepository( get(), get()) as ITodoRepository }
    single { TodoViewModel( get() ) }
}