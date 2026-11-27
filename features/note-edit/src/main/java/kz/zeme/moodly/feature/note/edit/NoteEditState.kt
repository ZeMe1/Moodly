package kz.zeme.moodly.feature.note.edit

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.moodly.core.note.domain.Emotion

@Immutable
data class NoteEditState(
    val emotions: ImmutableList<Emotion> = persistentListOf(),
    val selectedEmotion: Emotion = Emotion.Anger,
    val text: String = "",
    val createdAtLabel: String = "",
    val canSave: Boolean = false,
    val isSaving: Boolean = false,
)
