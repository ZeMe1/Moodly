package kz.zeme.moodly.core.navigation.di

import kz.zeme.moodly.core.navigation.NavigationResultLiveData
import org.koin.dsl.module

val coreNavigationModule = module {
    single { NavigationResultLiveData() }
}
