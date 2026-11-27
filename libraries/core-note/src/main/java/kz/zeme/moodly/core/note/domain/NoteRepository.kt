package kz.zeme.moodly.core.note.domain

import kz.zeme.moodly.core.domain.Response

interface NoteRepository {

    suspend fun getAllNotes(): Response<List<Note>>

    suspend fun getNotesByEmotion(emotion: Emotion): Response<List<Note>>

    suspend fun getNoteById(id: Long): Response<Note>

    suspend fun createNote(
        emotion: Emotion,
        text: String,
        createdAt: Long = System.currentTimeMillis(),
    ): Response<Long>

    suspend fun updateNote(
        id: Long,
        emotion: Emotion,
        text: String,
    ): Response<Unit>

    suspend fun deleteNote(id: Long): Response<Unit>
}
