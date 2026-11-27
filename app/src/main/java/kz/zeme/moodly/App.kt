package kz.zeme.moodly

import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import kz.zeme.moodly.core.theme.domain.ThemeModeProvider
import kz.zeme.moodly.di.KoinController
import kz.zeme.moodly.di.KoinModules
import org.koin.android.ext.android.get

open class App : Application() {

    open val koinModules: KoinModules = KoinModules()

    override fun onCreate() {
        super.onCreate()

        KoinController.onStart(
            application = this,
            modules = koinModules.getModules(),
        )

        get<ThemeModeProvider>().applySelectedTheme()
        registerActivityLifecycleCallbacks(get<ActivityLifecycleCallbacks>())
    }
}
