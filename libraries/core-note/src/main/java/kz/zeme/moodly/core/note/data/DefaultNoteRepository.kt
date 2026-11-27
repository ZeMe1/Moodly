package kz.zeme.moodly.core.note.data

import kotlinx.coroutines.CancellationException
import kz.zeme.moodly.core.domain.Response
import kz.zeme.moodly.core.error.NotFoundError
import kz.zeme.moodly.core.error.UnexpectedError
import kz.zeme.moodly.core.error.ValidationError
import kz.zeme.moodly.core.note.data.local.db.NoteDbDao
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.Note
import kz.zeme.moodly.core.note.domain.NoteRepository

class DefaultNoteRepository(
    private val localDataSource: NoteDbDao,
    private val dbEntityMapper: NoteDbEntityMapper,
) : NoteRepository {

    override suspend fun getAllNotes(): Response<List<Note>> = executeSafe {
        val entities = localDataSource.getAllNotes()
        Response.Success(entities.map(dbEntityMapper::mapToDomain))
    }

    override suspend fun getNotesByEmotion(emotion: Emotion): Response<List<Note>> = executeSafe {
        val entities = localDataSource.getNotesByEmotion(emotion.code)
        Response.Success(entities.map(dbEntityMapper::mapToDomain))
    }

    override suspend fun getNoteById(id: Long): Response<Note> = executeSafe {
        val entity = localDataSource.getNoteById(id)
            ?: return@executeSafe Response.Failure(NotFoundError())

        Response.Success(dbEntityMapper.mapToDomain(entity))
    }

    override suspend fun createNote(
        emotion: Emotion,
        text: String,
        createdAt: Long,
    ): Response<Long> = executeSafe {
        val normalizedText = text.trim()
        if (normalizedText.isEmpty()) return@executeSafe Response.Failure(ValidationError())

        val entity = dbEntityMapper.mapToDbEntity(
            domain = Note(
                emotion = emotion,
                text = normalizedText,
                createdAt = createdAt,
            ),
        )
        val noteId = localDataSource.insertNote(entity)
        Response.Success(noteId)
    }

    override suspend fun updateNote(
        id: Long,
        emotion: Emotion,
        text: String,
    ): Response<Unit> = executeSafe {
        val normalizedText = text.trim()
        if (normalizedText.isEmpty()) return@executeSafe Response.Failure(ValidationError())

        val updatedCount = localDataSource.updateNote(
            id = id,
            emotion = emotion.code,
            text = normalizedText,
        )
        if (updatedCount == 0) Response.Failure(NotFoundError())
        else Response.Success(Unit)
    }

    override suspend fun deleteNote(id: Long): Response<Unit> = executeSafe {
        localDataSource.deleteNoteById(id)
        Response.Success(Unit)
    }

    private inline fun <T : Any> executeSafe(block: () -> Response<T>): Response<T> = try {
        block()
    } catch (ex: CancellationException) {
        throw ex
    } catch (ex: Exception) {
        Response.Failure(UnexpectedError(underlying = ex, message = ex.message))
    }
}
