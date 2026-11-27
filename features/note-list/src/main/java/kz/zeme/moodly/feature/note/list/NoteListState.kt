package kz.zeme.moodly.feature.note.list

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.moodly.core.note.domain.Emotion

@Immutable
data class NoteListState(
    val dayGroups: ImmutableList<NoteDayGroup> = persistentListOf(),
)

@Immutable
data class NoteDayGroup(
    val dayTitle: String,
    val notes: ImmutableList<NoteCardItem> = persistentListOf(),
)

@Immutable
data class NoteCardItem(
    val id: Long,
    val emotion: Emotion,
    val timeText: String,
    val text: String,
)
