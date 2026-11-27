package kz.zeme.moodly.core.note.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteDbEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "emotion")
    val emotion: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
