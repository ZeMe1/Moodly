package kz.zeme.moodly.features

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kz.zeme.moodly.core.activity.ActivityProvider
import kz.zeme.moodly.core.activity.extensions.enableEdgeToEdge
import kz.zeme.moodly.core.activity.extensions.setLocale
import kz.zeme.moodly.core.locale.domain.LocaleRepository
import kz.zeme.moodly.core.theme.domain.ThemeModeProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class AppGlobalActivityProvider :
    Application.ActivityLifecycleCallbacks,
    ActivityProvider,
    KoinComponent {

    private val localeRepository: LocaleRepository by inject()
    private val themeModeProvider: ThemeModeProvider by inject()

    override var currentActivity: AppCompatActivity? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity as? AppCompatActivity
        themeModeProvider.applySelectedTheme()
        activity.setLocale(localeRepository.getSelectedLocale())
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity as? AppCompatActivity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity as? AppCompatActivity
        themeModeProvider.applySelectedTheme()
        currentActivity?.enableEdgeToEdge(themeModeProvider.isDarkTheme())
        activity.setLocale(localeRepository.getSelectedLocale())
    }

    override fun onActivityPaused(activity: Activity) = clear(activity)

    override fun onActivityStopped(activity: Activity) = clear(activity)

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = clear(activity)

    override fun onActivityDestroyed(activity: Activity) = clear(activity)

    private fun clear(activity: Activity) {
        if (currentActivity != activity) return
        currentActivity = null
    }
}
