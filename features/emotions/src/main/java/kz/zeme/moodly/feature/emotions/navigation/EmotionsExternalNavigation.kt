package kz.zeme.moodly.feature.emotions.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.note.domain.Emotion

@Parcelize
sealed class EmotionsExternalNavigation : Navigator.Navigation {

    data class ShowEmotionsList(
        val emotion: Emotion,
    ) : EmotionsExternalNavigation()

    data object ShowNotesTab : EmotionsExternalNavigation()

    data object ShowSettingsTab : EmotionsExternalNavigation()
}
