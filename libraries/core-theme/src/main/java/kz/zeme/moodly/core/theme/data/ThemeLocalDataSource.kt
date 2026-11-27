package kz.zeme.moodly.core.theme.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode

class ThemeLocalDataSource(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val THEME_MODE_KEY = "THEME_MODE_KEY"
    }

    fun getSelectedThemeMode(defaultMode: ThemeMode = ThemeMode.Dark): ThemeMode =
        sharedPreferences.getString(THEME_MODE_KEY, defaultMode.code)
            ?.let { ThemeMode.find(code = it, default = defaultMode) }
            ?: defaultMode

    fun saveSelectedThemeMode(themeMode: ThemeMode?) {
        themeMode ?: return

        sharedPreferences.edit(commit = true) {
            putString(THEME_MODE_KEY, themeMode.code)
        }
    }
}
