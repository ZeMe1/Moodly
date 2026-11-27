package kz.zeme.moodly.feature.note.list.di

import kz.zeme.moodly.feature.note.list.NoteListVM
import kz.zeme.moodly.feature.note.list.navigation.NoteListRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noteListModule = module {
    factory { NoteListRouter() }

    viewModel {
        NoteListVM(
            noteRepository = get(),
            noteDateFormatter = get(),
            navigationResult = get(),
        )
    }
}
