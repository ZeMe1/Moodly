package kz.zeme.moodly.feature.emotions.list.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class EmotionsListExternalNavigation : Navigator.Navigation {

    data class ShowNoteDetail(
        val noteId: Long,
        val requestCode: RequestCode = RequestCode.RC_EMOTIONS_LIST_NOTE_DETAIL,
    ) : EmotionsListExternalNavigation()
}
