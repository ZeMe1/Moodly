package kz.zeme.moodly.feature.note.detail.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class NoteDetailExternalNavigation : Navigator.Navigation {

    data class ShowNoteEdit(
        val noteId: Long,
        val requestCode: RequestCode = RequestCode.RC_NOTE_EDIT,
    ) : NoteDetailExternalNavigation()
}
