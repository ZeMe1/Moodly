package kz.zeme.moodly.feature.settings.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class SettingsExternalNavigation : Navigator.Navigation {

    data object ShowNotesTab : SettingsExternalNavigation()

    data object ShowEmotionsTab : SettingsExternalNavigation()
}
