package kz.zeme.moodly.core.theme.domain

import kz.zeme.moodly.core.domain.Response

interface ThemeRepository {

    suspend fun getThemes(): Response<List<AppTheme>>

    suspend fun saveSelectedTheme(theme: AppTheme): Response<Unit>
}
