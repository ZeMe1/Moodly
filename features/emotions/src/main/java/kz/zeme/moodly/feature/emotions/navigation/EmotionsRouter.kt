package kz.zeme.moodly.feature.emotions.navigation

import kz.zeme.moodly.feature.emotions.EmotionsFragment

class EmotionsRouter {

    companion object {
        const val TAG_EMOTIONS = "TAG_EMOTIONS"
    }

    fun getEmotionsFragment(): EmotionsFragment = EmotionsFragment()
}
