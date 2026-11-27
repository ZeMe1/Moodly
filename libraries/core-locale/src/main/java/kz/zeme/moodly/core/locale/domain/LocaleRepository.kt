package kz.zeme.moodly.core.locale.domain

import java.util.Locale

interface LocaleRepository {

    fun getSelectedLocale(): Locale
}
