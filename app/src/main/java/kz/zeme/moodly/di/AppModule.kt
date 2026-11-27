package kz.zeme.moodly.di

import android.app.Application.ActivityLifecycleCallbacks
import kz.zeme.moodly.core.activity.ActivityProvider
import kz.zeme.moodly.core.di.extensions.binds
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.features.AppGlobalActivityProvider
import kz.zeme.moodly.features.main.MainActivityVM
import kz.zeme.moodly.features.navigation.AppGlobalNavigator
import kz.zeme.moodly.features.navigation.deeplink.AppGlobalRedirectParser
import kz.zeme.moodly.features.navigation.deeplink.AppRedirectParser
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        AppGlobalActivityProvider()
    }.binds(
        ActivityProvider::class,
        ActivityLifecycleCallbacks::class,
    )

    factory {
        AppGlobalRedirectParser()
    }.binds(
        AppRedirectParser::class,
    )

    viewModel {
        MainActivityVM(
            appRedirectParser = get(),
        )
    }

    single {
        AppGlobalNavigator(
            activityProvider = get(),
            languagePickerRouter = get(),
        )
    }.binds(
        ExternalModulesNavigator::class,
    )
}
