package kz.zeme.moodly.core.navigation

import android.os.Parcelable

interface Navigator {

    interface Navigation : Parcelable

    fun navigate(navigation: Navigation)
}
