package kz.zeme.moodly.feature.settings

import kz.zeme.moodly.core.ui.components.MoodlyBottomTab

interface SettingsController {

    fun onThemeClick()

    fun onLanguageClick()

    fun onTabClick(tab: MoodlyBottomTab)
}
