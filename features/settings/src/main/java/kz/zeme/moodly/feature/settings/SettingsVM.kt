package kz.zeme.moodly.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.zeme.moodly.core.locale.view.presentation.navigation.LanguagePickerNavigation
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.observable.EventFlow
import kz.zeme.moodly.core.observable.MutableEventFlow
import kz.zeme.moodly.core.theme.view.presentation.navigation.ThemePickerNavigation
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab
import kz.zeme.moodly.feature.settings.navigation.SettingsExternalNavigation

class SettingsVM : ViewModel(), SettingsController {

    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    override fun onThemeClick() {
        _navigation.value = ThemePickerNavigation.OpenThemePicker
    }

    override fun onLanguageClick() {
        _navigation.value = LanguagePickerNavigation.OpenLanguagePicker
    }

    override fun onTabClick(tab: MoodlyBottomTab) = when (tab) {
        MoodlyBottomTab.Notes -> _navigation.value = SettingsExternalNavigation.ShowNotesTab
        MoodlyBottomTab.Emotions -> _navigation.value = SettingsExternalNavigation.ShowEmotionsTab
        MoodlyBottomTab.Settings -> Unit
    }
}
