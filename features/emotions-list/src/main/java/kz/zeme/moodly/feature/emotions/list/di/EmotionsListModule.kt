package kz.zeme.moodly.feature.emotions.list.di

import kz.zeme.moodly.feature.emotions.list.EmotionsListVM
import kz.zeme.moodly.feature.emotions.list.navigation.EmotionsListRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val emotionsListModule = module {
    factory { EmotionsListRouter() }

    viewModel { parameters ->
        EmotionsListVM(
            emotion = parameters.get(),
            noteRepository = get(),
            navigationResult = get(),
        )
    }
}
