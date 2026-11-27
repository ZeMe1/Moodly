package kz.zeme.moodly.core.res.theme

import androidx.compose.runtime.Composable

object MoodlyTheme {

    val colors: MoodlyColors
        @Composable
        get() = LocalStaticColors.current

    val typography: MoodlyTypography
        @Composable
        get() = LocalStaticTypography.current
}
