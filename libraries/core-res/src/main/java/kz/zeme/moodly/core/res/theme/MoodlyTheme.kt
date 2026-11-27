package kz.zeme.moodly.core.res.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object MoodlyTheme {

    var isDark: Boolean by mutableStateOf(true)
        private set

    val colors: MoodlyColors
        @Composable
        get() = if (isDark) LocalStaticColors.current else LocalLightStaticColors.current

    val typography: MoodlyTypography
        @Composable
        get() = LocalStaticTypography.current

    val emotionColors: MoodlyEmotionColors
        @Composable
        get() = LocalStaticEmotionColors.current

    fun applyTheme(isDark: Boolean) {
        this.isDark = isDark
    }
}
