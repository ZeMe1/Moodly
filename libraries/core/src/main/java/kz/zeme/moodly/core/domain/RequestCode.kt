package kz.zeme.moodly.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RequestCode : Parcelable {
    RC_NOTE_CREATE,
    RC_NOTE_DETAIL,
    RC_NOTE_EDIT,
    RC_EMOTIONS_LIST_NOTE_DETAIL,
    RC_NOTE_DELETE_CONFIRM,
}
