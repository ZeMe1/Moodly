package kz.zeme.moodly.core.theme.domain

data class AppTheme(
    val isSelected: Boolean,
    val mode: ThemeMode,
) {

    enum class ThemeMode(
        val code: String,
    ) {
        Light("light"),
        Dark("dark"),
        System("system");

        companion object {

            fun find(
                code: String?,
                default: ThemeMode = Dark,
            ): ThemeMode = entries
                .find { it.code.contentEquals(code) }
                ?: default
        }
    }
}
