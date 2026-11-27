package kz.zeme.moodly.core.locale.view.presentation.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class LanguagePickerNavigation : Navigator.Navigation {
    data object OpenLanguagePicker : LanguagePickerNavigation()
}
