package kz.zeme.moodly.core.note.di

import androidx.room.Room
import kz.zeme.moodly.core.di.extensions.binds
import kz.zeme.moodly.core.note.data.DefaultNoteRepository
import kz.zeme.moodly.core.note.data.NoteDbEntityMapper
import kz.zeme.moodly.core.note.data.local.db.NoteDb
import kz.zeme.moodly.core.note.domain.NoteRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val coreNoteModule = module {
    factory { NoteDbEntityMapper() }

    single<NoteDb> {
        Room.databaseBuilder(
            androidApplication(),
            NoteDb::class.java,
            "Note.db",
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    single {
        DefaultNoteRepository(
            localDataSource = get<NoteDb>().noteDao(),
            dbEntityMapper = get(),
        )
    }.binds(
        NoteRepository::class,
    )
}
