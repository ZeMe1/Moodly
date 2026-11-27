package kz.zeme.moodly.core.theme.view.presentation.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class ThemePickerNavigation : Navigator.Navigation {
    data object OpenThemePicker : ThemePickerNavigation()
}
