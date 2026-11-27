package kz.zeme.moodly.core.theme.view.presentation.navigation

import kz.zeme.moodly.core.fragment.BaseBottomSheetDialog
import kz.zeme.moodly.core.theme.view.presentation.ThemePickerFragment

class ThemePickerRouter {
    companion object {
        const val TAG_THEME_PICKER = "TAG_THEME_PICKER"
    }

    fun getThemePickerFragment(): BaseBottomSheetDialog =
        ThemePickerFragment()
}
