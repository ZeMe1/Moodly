package kz.zeme.moodly.core.note.data

import kz.zeme.moodly.core.note.data.local.db.NoteDbEntity
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.Note
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class NoteDbEntityMapper {

    fun mapToDomain(entity: NoteDbEntity): Note = Note(
        id = entity.id,
        emotion = Emotion.find(entity.emotion),
        text = entity.text,
        createdAt = entity.createdAt,
        formattedDate = formatFullDateTime(entity.createdAt),
    )

    fun mapToDbEntity(domain: Note): NoteDbEntity = NoteDbEntity(
        id = domain.id,
        emotion = domain.emotion.code,
        text = domain.text,
        createdAt = domain.createdAt,
    )

    private fun formatFullDateTime(timestampMs: Long): String {
        val dateTime = Instant.ofEpochMilli(timestampMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return DateTimeFormatter
            .ofPattern("d MMMM yyyy, HH:mm", Locale.getDefault())
            .format(dateTime)
    }
}
