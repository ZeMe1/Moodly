package kz.zeme.moodly.core.theme.view.presentation

import kz.zeme.moodly.core.theme.domain.AppTheme

interface ThemePickerController {
    fun onThemeClick(theme: AppTheme)
    fun onClose()
    fun onStart()
}
