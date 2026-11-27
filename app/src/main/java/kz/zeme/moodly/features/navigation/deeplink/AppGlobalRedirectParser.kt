package kz.zeme.moodly.features.navigation.deeplink

import android.content.Intent
import android.os.Bundle
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.navigation.extensions.getRedirectOrNull

class AppGlobalRedirectParser : AppRedirectParser {

    override fun getRedirectOrNull(intent: Intent?): Navigator.Navigation? {
        intent ?: return null

        intent.getRedirectOrNull()?.let {
            if (it is BaseNavigation.ShowMain) return it
            return BaseNavigation.ShowMain(restart = false, redirect = it, withSplash = false)
        }

        intent.data?.let {
            return BaseNavigation.ShowDeeplink(url = it.toString())
        }

        return null
    }

    override fun getRedirectOrNull(bundle: Bundle?): Navigator.Navigation? =
        bundle?.getRedirectOrNull()
}
