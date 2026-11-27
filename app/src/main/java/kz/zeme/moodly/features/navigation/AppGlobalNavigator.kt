package kz.zeme.moodly.features.navigation

import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.commit
import kz.zeme.moodly.MainActivity
import kz.zeme.moodly.R
import kz.zeme.moodly.core.activity.ActivityProvider
import kz.zeme.moodly.core.fragment.extensions.isShown
import kz.zeme.moodly.core.fragment.extensions.makeToast
import kz.zeme.moodly.core.fragment.extensions.onBackPressed
import kz.zeme.moodly.core.locale.view.presentation.navigation.LanguagePickerNavigation
import kz.zeme.moodly.core.locale.view.presentation.navigation.LanguagePickerRouter
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.navigation.extensions.putRedirect
import kz.zeme.moodly.splash.SplashExternalNavigation
import kz.zeme.moodly.splash.SplashFragment

class AppGlobalNavigator(
    private val activityProvider: ActivityProvider,
    private val languagePickerRouter: LanguagePickerRouter,
) : ExternalModulesNavigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack -> goBack()
        is BaseNavigation.Close -> close()
        is BaseNavigation.ShowMain -> showMain(navigation)
        is BaseNavigation.ShowToast -> showToast(navigation)
        is BaseNavigation.ShowDeeplink -> showDeeplink(navigation)
        is LanguagePickerNavigation.OpenLanguagePicker -> openLanguagePicker()
        is SplashExternalNavigation.ShowNextScreen -> showSplashNextScreen(navigation)
        else -> Unit
    }

    private fun goBack() {
        activityProvider.currentActivity?.supportFragmentManager?.onBackPressed()
    }

    private fun close() {
        activityProvider.currentActivity?.supportFragmentManager?.onBackPressed()
    }

    private fun showMain(navigation: BaseNavigation.ShowMain) {
        val activity = activityProvider.currentActivity ?: return

        if (navigation.restart) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putRedirect(navigation.redirect)
            }
            activity.finish()
            activity.startActivity(intent)
            return
        }

        val fm = activity.supportFragmentManager

        if (navigation.withSplash) {
            val hasSplash = fm.fragments
                .filterIsInstance<SplashFragment>()
                .isNotEmpty()

            if (hasSplash) return

            fm.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, SplashFragment())
            }
            return
        }

        navigation.redirect?.let(::navigate)
    }

    private fun openLanguagePicker() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = LanguagePickerRouter.TAG_LANGUAGE_PICKER

        if (fm.isShown(tag)) return

        languagePickerRouter
            .getLanguagePickerFragment()
            .show(fm, tag)
    }

    private fun showSplashNextScreen(navigation: SplashExternalNavigation.ShowNextScreen) {
        val redirect = (navigation.redirect as? BaseNavigation.ShowMain)?.redirect ?: navigation.redirect
        showMain(
            BaseNavigation.ShowMain(
                restart = false,
                redirect = redirect,
                withSplash = false,
            ),
        )
    }

    private fun showToast(navigation: BaseNavigation.ShowToast) {
        activityProvider.currentActivity?.makeToast(navigation)
    }

    private fun showDeeplink(navigation: BaseNavigation.ShowDeeplink) {
        val context = activityProvider.currentActivity ?: return
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, navigation.url.toUri()))
        }
    }
}
