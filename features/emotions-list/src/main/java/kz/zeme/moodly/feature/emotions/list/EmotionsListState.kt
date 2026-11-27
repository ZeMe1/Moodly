package kz.zeme.moodly.feature.emotions.list

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.Note

@Immutable
data class EmotionsListState(
    val emotion: Emotion = Emotion.Anger,
    val notes: ImmutableList<Note> = persistentListOf(),
)
