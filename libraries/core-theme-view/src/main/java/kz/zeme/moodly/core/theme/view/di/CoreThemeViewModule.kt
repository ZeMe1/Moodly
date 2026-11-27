package kz.zeme.moodly.core.theme.view.di

import kz.zeme.moodly.core.theme.view.presentation.ThemePickerVM
import kz.zeme.moodly.core.theme.view.presentation.navigation.ThemePickerRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreThemeViewModule = module {
    factory { ThemePickerRouter() }

    viewModel {
        ThemePickerVM(
            themeRepository = get(),
        )
    }
}
