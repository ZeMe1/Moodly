package kz.zeme.moodly.feature.note.create.di

import kz.zeme.moodly.feature.note.create.NoteCreateVM
import kz.zeme.moodly.feature.note.create.navigation.NoteCreateRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noteCreateModule = module {
    factory { NoteCreateRouter() }

    viewModel { parameters ->
        NoteCreateVM(
            requestCode = parameters.get(),
            noteRepository = get(),
            noteDateFormatter = get(),
            navigationResult = get(),
        )
    }
}
