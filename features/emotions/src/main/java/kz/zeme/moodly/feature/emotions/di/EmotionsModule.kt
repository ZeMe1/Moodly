package kz.zeme.moodly.feature.emotions.di

import kz.zeme.moodly.feature.emotions.EmotionsVM
import kz.zeme.moodly.feature.emotions.navigation.EmotionsRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val emotionsModule = module {
    factory { EmotionsRouter() }

    viewModel { EmotionsVM() }
}
