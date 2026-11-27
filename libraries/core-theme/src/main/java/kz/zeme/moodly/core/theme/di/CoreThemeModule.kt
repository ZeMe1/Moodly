package kz.zeme.moodly.core.theme.di

import android.content.Context
import kz.zeme.moodly.core.di.extensions.binds
import kz.zeme.moodly.core.theme.data.DefaultThemeRepository
import kz.zeme.moodly.core.theme.data.ThemeLocalDataSource
import kz.zeme.moodly.core.theme.domain.ThemeModeProvider
import kz.zeme.moodly.core.theme.domain.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val THEME_PREFERENCES_NAME = "theme_preferences"

val coreThemeModule = module {
    single {
        ThemeLocalDataSource(
            sharedPreferences = androidContext().getSharedPreferences(
                THEME_PREFERENCES_NAME,
                Context.MODE_PRIVATE,
            ),
        )
    }

    single {
        DefaultThemeRepository(
            context = androidContext(),
            activityProvider = get(),
            themeLocalDataSource = get(),
        )
    }.binds(
        ThemeRepository::class,
        ThemeModeProvider::class,
    )
}
