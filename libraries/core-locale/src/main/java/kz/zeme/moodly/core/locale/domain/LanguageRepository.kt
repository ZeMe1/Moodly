package kz.zeme.moodly.core.locale.domain

import kz.zeme.moodly.core.domain.Response

interface LanguageRepository {

    suspend fun getLanguages(): Response<List<Language>>

    suspend fun saveSelectedLanguage(language: Language): Response<Unit>
}
