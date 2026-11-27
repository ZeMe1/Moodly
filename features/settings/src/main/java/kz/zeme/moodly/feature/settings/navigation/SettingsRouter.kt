package kz.zeme.moodly.feature.settings.navigation

import kz.zeme.moodly.feature.settings.SettingsFragment

class SettingsRouter {

    companion object {
        const val TAG_SETTINGS = "TAG_SETTINGS"
    }

    fun getSettingsFragment(): SettingsFragment = SettingsFragment()
}
