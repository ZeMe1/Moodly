package kz.zeme.moodly.core.note.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteDbEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class NoteDb : RoomDatabase() {

    abstract fun noteDao(): NoteDbDao
}
