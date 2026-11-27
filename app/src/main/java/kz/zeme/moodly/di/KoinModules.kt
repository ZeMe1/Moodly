package kz.zeme.moodly.di

import kz.zeme.moodly.core.locale.di.coreLocaleModule
import kz.zeme.moodly.core.locale.view.di.coreLocaleViewModule
import kz.zeme.moodly.core.navigation.di.coreNavigationModule
import kz.zeme.moodly.core.ui.di.coreUiModule
import org.koin.core.module.Module

open class KoinModules {

    open fun getModules(): List<Module> = listOf(
        coreNavigationModule,
        coreUiModule,
        coreLocaleModule,
        coreLocaleViewModule,
        appModule,
    )
}
