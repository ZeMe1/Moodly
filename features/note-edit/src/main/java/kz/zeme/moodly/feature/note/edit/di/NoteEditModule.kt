package kz.zeme.moodly.feature.note.edit.di

import kz.zeme.moodly.feature.note.edit.NoteEditVM
import kz.zeme.moodly.feature.note.edit.navigation.NoteEditRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noteEditModule = module {
    factory { NoteEditRouter() }

    viewModel { parameters ->
        NoteEditVM(
            noteId = parameters.get(),
            requestCode = parameters.get(),
            noteRepository = get(),
            noteDateFormatter = get(),
            navigationResult = get(),
        )
    }
}
