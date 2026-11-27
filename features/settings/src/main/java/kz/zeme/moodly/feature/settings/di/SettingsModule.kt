package kz.zeme.moodly.feature.settings.di

import kz.zeme.moodly.feature.settings.SettingsVM
import kz.zeme.moodly.feature.settings.navigation.SettingsRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory { SettingsRouter() }

    viewModel { SettingsVM() }
}
