package kz.zeme.moodly.core.locale.view.presentation

import kz.zeme.moodly.core.locale.domain.Language

interface LanguagePickerController {
    fun onLanguageClick(language: Language)
    fun onClose()
    fun onStart()
}
