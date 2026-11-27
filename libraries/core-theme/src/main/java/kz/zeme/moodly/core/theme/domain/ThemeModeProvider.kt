package kz.zeme.moodly.core.theme.domain

import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode

interface ThemeModeProvider {

    fun getSelectedThemeMode(): ThemeMode

    fun isDarkTheme(): Boolean

    fun applySelectedTheme()
}
