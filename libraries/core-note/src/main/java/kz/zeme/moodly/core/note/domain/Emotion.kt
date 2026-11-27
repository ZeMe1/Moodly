package kz.zeme.moodly.core.note.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Emotion(
    val code: String,
) : Parcelable {
    Anger("anger"),
    Joy("joy"),
    Sadness("sadness"),
    Calm("calm"),
    Fear("fear"),
    Anxiety("anxiety"),
    Surprise("surprise"),
    Tiredness("tiredness");

    companion object {

        fun find(
            code: String?,
            default: Emotion = Anger,
        ): Emotion = entries
            .find { it.code.contentEquals(code) }
            ?: default
    }
}
