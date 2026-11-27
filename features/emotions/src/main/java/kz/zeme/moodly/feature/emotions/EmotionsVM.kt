package kz.zeme.moodly.feature.emotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.observable.EventFlow
import kz.zeme.moodly.core.observable.MutableEventFlow
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab
import kz.zeme.moodly.feature.emotions.navigation.EmotionsExternalNavigation

class EmotionsVM : ViewModel(), EmotionsController {

    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    override fun onEmotionClick(emotion: Emotion) {
        _navigation.value = EmotionsExternalNavigation.ShowEmotionsList(emotion = emotion)
    }

    override fun onTabClick(tab: MoodlyBottomTab) = when (tab) {
        MoodlyBottomTab.Notes -> _navigation.value = EmotionsExternalNavigation.ShowNotesTab
        MoodlyBottomTab.Emotions -> Unit
        MoodlyBottomTab.Settings -> _navigation.value = EmotionsExternalNavigation.ShowSettingsTab
    }
}
