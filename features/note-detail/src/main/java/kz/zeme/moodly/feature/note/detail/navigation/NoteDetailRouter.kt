package kz.zeme.moodly.feature.note.detail.navigation

import android.os.Bundle
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.navigation.extensions.getTypedParcelable
import kz.zeme.moodly.feature.note.detail.DeleteNoteDialogFragment
import kz.zeme.moodly.feature.note.detail.NoteDetailFragment

class NoteDetailRouter {

    companion object {
        const val TAG_NOTE_DETAIL = "TAG_NOTE_DETAIL"
        const val TAG_DELETE_NOTE_DIALOG = "TAG_DELETE_NOTE_DIALOG"

        private const val NOTE_ID_KEY = "NOTE_ID_KEY"
        private const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
    }

    fun getNoteDetailFragment(
        noteId: Long,
        requestCode: RequestCode = RequestCode.RC_NOTE_DETAIL,
    ): NoteDetailFragment = NoteDetailFragment().apply {
        arguments = Bundle().apply {
            putLong(NOTE_ID_KEY, noteId)
            putParcelable(REQUEST_CODE_KEY, requestCode)
        }
    }

    fun getDeleteNoteDialog(): DeleteNoteDialogFragment =
        DeleteNoteDialogFragment()

    fun getNoteIdOrNull(bundle: Bundle?): Long? =
        if (bundle?.containsKey(NOTE_ID_KEY) == true) bundle.getLong(NOTE_ID_KEY)
        else null

    fun getRequestCodeOrNull(bundle: Bundle?): RequestCode? =
        bundle?.getTypedParcelable(REQUEST_CODE_KEY)
}
