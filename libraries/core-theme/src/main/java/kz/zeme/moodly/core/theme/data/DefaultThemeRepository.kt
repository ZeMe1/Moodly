package kz.zeme.moodly.core.theme.data

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import kz.zeme.moodly.core.activity.ActivityProvider
import kz.zeme.moodly.core.activity.extensions.enableEdgeToEdge
import kz.zeme.moodly.core.domain.Response
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.theme.domain.AppTheme
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode.Dark
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode.Light
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode.System
import kz.zeme.moodly.core.theme.domain.ThemeModeProvider
import kz.zeme.moodly.core.theme.domain.ThemeRepository

class DefaultThemeRepository(
    private val context: Context,
    private val activityProvider: ActivityProvider,
    private val themeLocalDataSource: ThemeLocalDataSource,
) : ThemeRepository, ThemeModeProvider {

    override suspend fun getThemes(): Response<List<AppTheme>> {
        val selectedMode = themeLocalDataSource.getSelectedThemeMode()

        return Response.Success(
            listOf(
                AppTheme(
                    isSelected = selectedMode == Light,
                    mode = Light,
                ),
                AppTheme(
                    isSelected = selectedMode == Dark,
                    mode = Dark,
                ),
                AppTheme(
                    isSelected = selectedMode == System,
                    mode = System,
                ),
            ),
        )
    }

    override suspend fun saveSelectedTheme(theme: AppTheme): Response<Unit> {
        themeLocalDataSource.saveSelectedThemeMode(theme.mode)
        applyTheme(theme.mode)
        return Response.Success(Unit)
    }

    override fun getSelectedThemeMode(): ThemeMode =
        themeLocalDataSource.getSelectedThemeMode()

    override fun isDarkTheme(): Boolean =
        isDarkTheme(getSelectedThemeMode())

    override fun applySelectedTheme() {
        applyTheme(getSelectedThemeMode())
    }

    private fun isDarkTheme(mode: ThemeMode): Boolean = when (mode) {
        Light -> false
        Dark -> true
        System -> isSystemDarkTheme()
    }

    private fun isSystemDarkTheme(): Boolean {
        val activeContext = activityProvider.currentActivity ?: context
        val nightModeFlags = activeContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    private fun applyTheme(mode: ThemeMode) {
        val isDark = isDarkTheme(mode)
        MoodlyTheme.applyTheme(isDark)

        val nightMode = when (mode) {
            Light -> AppCompatDelegate.MODE_NIGHT_NO
            Dark -> AppCompatDelegate.MODE_NIGHT_YES
            System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)

        activityProvider.currentActivity?.enableEdgeToEdge(isDark)
    }
}
