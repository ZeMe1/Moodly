package kz.zeme.moodly.core.ui.di

import kz.zeme.moodly.core.ui.formatter.NoteDateFormatter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreUiModule = module {
    factory {
        NoteDateFormatter(
            context = androidContext(),
            activityProvider = get(),
        )
    }
}
