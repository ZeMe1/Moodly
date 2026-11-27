package kz.zeme.moodly.feature.note.edit.navigation

import android.os.Bundle
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.navigation.extensions.getTypedParcelable
import kz.zeme.moodly.feature.note.edit.NoteEditFragment

class NoteEditRouter {

    companion object {
        const val TAG_NOTE_EDIT = "TAG_NOTE_EDIT"

        private const val NOTE_ID_KEY = "NOTE_ID_KEY"
        private const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
    }

    fun getNoteEditFragment(
        noteId: Long,
        requestCode: RequestCode = RequestCode.RC_NOTE_EDIT,
    ): NoteEditFragment = NoteEditFragment().apply {
        arguments = Bundle().apply {
            putLong(NOTE_ID_KEY, noteId)
            putParcelable(REQUEST_CODE_KEY, requestCode)
        }
    }

    fun getNoteIdOrNull(bundle: Bundle?): Long? =
        if (bundle?.containsKey(NOTE_ID_KEY) == true) bundle.getLong(NOTE_ID_KEY)
        else null

    fun getRequestCodeOrNull(bundle: Bundle?): RequestCode? =
        bundle?.getTypedParcelable(REQUEST_CODE_KEY)
}
