package kz.zeme.moodly.feature.note.create.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class NoteCreateExternalNavigation : Navigator.Navigation {

    data class ShowNoteCreate(
        val requestCode: RequestCode = RequestCode.RC_NOTE_CREATE,
    ) : NoteCreateExternalNavigation()
}
