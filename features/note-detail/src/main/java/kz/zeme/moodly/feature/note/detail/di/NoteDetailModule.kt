package kz.zeme.moodly.feature.note.detail.di

import kz.zeme.moodly.feature.note.detail.NoteDetailVM
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailNavigator
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noteDetailModule = module {
    factory { NoteDetailRouter() }

    factory {
        NoteDetailNavigator(
            activityProvider = get(),
            noteDetailRouter = get(),
            externalModulesNavigator = get(),
        )
    }

    viewModel { parameters ->
        NoteDetailVM(
            noteId = parameters.get(),
            requestCode = parameters.get(),
            noteRepository = get(),
            navigationResult = get(),
        )
    }
}
