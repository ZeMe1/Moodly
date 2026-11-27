package kz.zeme.moodly.core.note.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDbDao {

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    suspend fun getAllNotes(): List<NoteDbEntity>

    @Query("SELECT * FROM notes WHERE emotion = :emotion ORDER BY created_at DESC")
    suspend fun getNotesByEmotion(emotion: String): List<NoteDbEntity>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteDbEntity): Long

    @Query("UPDATE notes SET emotion = :emotion, text = :text WHERE id = :id")
    suspend fun updateNote(
        id: Long,
        emotion: String,
        text: String,
    ): Int

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)
}
