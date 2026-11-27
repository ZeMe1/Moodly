package kz.zeme.moodly.feature.emotions

import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab

interface EmotionsController {

    fun onEmotionClick(emotion: Emotion)

    fun onTabClick(tab: MoodlyBottomTab)
}
