package kz.zeme.moodly.core.note.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Long = 0L,
    val emotion: Emotion = Emotion.Anger,
    val text: String = "",
    val createdAt: Long = 0L,
    val formattedDate: String = "",
) : Parcelable
