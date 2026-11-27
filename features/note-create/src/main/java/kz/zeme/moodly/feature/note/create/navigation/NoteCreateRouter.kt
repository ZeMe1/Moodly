package kz.zeme.moodly.feature.note.create.navigation

import android.os.Bundle
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.navigation.extensions.getTypedParcelable
import kz.zeme.moodly.feature.note.create.NoteCreateFragment

class NoteCreateRouter {

    companion object {
        const val TAG_NOTE_CREATE = "TAG_NOTE_CREATE"
        private const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
    }

    fun getNoteCreateFragment(
        requestCode: RequestCode = RequestCode.RC_NOTE_CREATE,
    ): NoteCreateFragment = NoteCreateFragment().apply {
        arguments = Bundle().apply {
            putParcelable(REQUEST_CODE_KEY, requestCode)
        }
    }

    fun getRequestCodeOrNull(bundle: Bundle?): RequestCode? =
        bundle?.getTypedParcelable(REQUEST_CODE_KEY)
}
