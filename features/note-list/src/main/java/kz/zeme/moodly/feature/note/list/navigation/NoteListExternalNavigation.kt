package kz.zeme.moodly.feature.note.list.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class NoteListExternalNavigation : Navigator.Navigation {

    data class ShowNoteCreate(
        val requestCode: RequestCode = RequestCode.RC_NOTE_CREATE,
    ) : NoteListExternalNavigation()

    data class ShowNoteDetail(
        val noteId: Long,
        val requestCode: RequestCode = RequestCode.RC_NOTE_DETAIL,
    ) : NoteListExternalNavigation()

    data object ShowEmotionsTab : NoteListExternalNavigation()

    data object ShowSettingsTab : NoteListExternalNavigation()
}
