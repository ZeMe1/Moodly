package kz.zeme.moodly.feature.note.create

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.moodly.core.note.domain.Emotion

@Immutable
data class NoteCreateState(
    val emotions: ImmutableList<Emotion> = persistentListOf(),
    val selectedEmotion: Emotion = Emotion.Anger,
    val text: String = "",
    val dateLabel: String = "",
    val canSave: Boolean = false,
    val isSaving: Boolean = false,
)
