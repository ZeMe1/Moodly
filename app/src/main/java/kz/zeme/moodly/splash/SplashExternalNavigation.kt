package kz.zeme.moodly.splash

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class SplashExternalNavigation : Navigator.Navigation {

    data class ShowNextScreen(
        val redirect: Navigator.Navigation? = null,
    ) : SplashExternalNavigation()
}
