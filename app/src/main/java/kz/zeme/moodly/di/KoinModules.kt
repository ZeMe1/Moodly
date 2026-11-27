package kz.zeme.moodly.di

import kz.zeme.moodly.core.locale.di.coreLocaleModule
import kz.zeme.moodly.core.locale.view.di.coreLocaleViewModule
import kz.zeme.moodly.core.navigation.di.coreNavigationModule
import kz.zeme.moodly.core.note.di.coreNoteModule
import kz.zeme.moodly.core.theme.di.coreThemeModule
import kz.zeme.moodly.core.theme.view.di.coreThemeViewModule
import kz.zeme.moodly.core.ui.di.coreUiModule
import kz.zeme.moodly.feature.emotions.di.emotionsModule
import kz.zeme.moodly.feature.emotions.list.di.emotionsListModule
import kz.zeme.moodly.feature.note.create.di.noteCreateModule
import kz.zeme.moodly.feature.note.detail.di.noteDetailModule
import kz.zeme.moodly.feature.note.edit.di.noteEditModule
import kz.zeme.moodly.feature.note.list.di.noteListModule
import kz.zeme.moodly.feature.settings.di.settingsModule
import org.koin.core.module.Module

open class KoinModules {

    open fun getModules(): List<Module> = listOf(
        coreNavigationModule,
        coreUiModule,
        coreLocaleModule,
        coreLocaleViewModule,
        coreThemeModule,
        coreThemeViewModule,
        coreNoteModule,
        noteListModule,
        noteCreateModule,
        noteDetailModule,
        noteEditModule,
        emotionsModule,
        emotionsListModule,
        settingsModule,
        appModule,
    )
}
