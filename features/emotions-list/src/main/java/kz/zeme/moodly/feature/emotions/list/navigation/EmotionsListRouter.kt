package kz.zeme.moodly.feature.emotions.list.navigation

import android.os.Bundle
import kz.zeme.moodly.core.navigation.extensions.getTypedParcelable
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.feature.emotions.list.EmotionsListFragment

class EmotionsListRouter {

    companion object {
        const val TAG_EMOTIONS_LIST = "TAG_EMOTIONS_LIST"

        private const val EMOTION_KEY = "EMOTION_KEY"
    }

    fun getEmotionsListFragment(emotion: Emotion): EmotionsListFragment =
        EmotionsListFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EMOTION_KEY, emotion)
            }
        }

    fun getEmotionOrNull(bundle: Bundle?): Emotion? =
        bundle?.getTypedParcelable(EMOTION_KEY)
}
