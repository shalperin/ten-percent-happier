package com.blauhaus.android.redwood.sample

import com.blauhaus.android.redwood.sample.data.IRepository
import com.blauhaus.android.redwood.sample.data.Repository
import com.blauhaus.android.redwood.sample.components.meditationdemo.achievementpager.AchievementViewModel
import org.koin.dsl.module


val mainModule = module {
    single { Repository() as IRepository }
    single {
        AchievementViewModel(
            get()
        )
    }
}
