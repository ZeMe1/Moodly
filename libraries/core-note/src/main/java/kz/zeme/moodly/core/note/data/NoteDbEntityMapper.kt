package kz.zeme.moodly.core.note.data

import kz.zeme.moodly.core.note.data.local.db.NoteDbEntity
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.Note

class NoteDbEntityMapper {

    fun mapToDomain(entity: NoteDbEntity): Note = Note(
        id = entity.id,
        emotion = Emotion.find(entity.emotion),
        text = entity.text,
        createdAt = entity.createdAt,
    )

    fun mapToDbEntity(domain: Note): NoteDbEntity = NoteDbEntity(
        id = domain.id,
        emotion = domain.emotion.code,
        text = domain.text,
        createdAt = domain.createdAt,
    )
}
