package kz.zeme.moodly.core.res.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MoodlyEmotionColors(
    val anger: Color,
    val joy: Color,
    val sadness: Color,
    val calm: Color,
    val fear: Color,
    val anxiety: Color,
    val surprise: Color,
    val tiredness: Color,
    val onEmotion: Color,
    val onDanger: Color,
)

internal val Emotion_Anger = Color(color = 0xFFE06C6C)
internal val Emotion_Joy = Color(color = 0xFFF2C86B)
internal val Emotion_Sadness = Color(color = 0xFF7BA3E8)
internal val Emotion_Calm = Color(color = 0xFF6CCFA8)
internal val Emotion_Fear = Color(color = 0xFFA98BE0)
internal val Emotion_Anxiety = Color(color = 0xFFEE9A62)
internal val Emotion_Surprise = Color(color = 0xFFE58FC0)
internal val Emotion_Tiredness = Color(color = 0xFFA4A9B6)
internal val On_Emotion = Color(color = 0xFF16171B)
internal val On_Danger = Color.White

val DefaultMoodlyEmotionColors = MoodlyEmotionColors(
    anger = Emotion_Anger,
    joy = Emotion_Joy,
    sadness = Emotion_Sadness,
    calm = Emotion_Calm,
    fear = Emotion_Fear,
    anxiety = Emotion_Anxiety,
    surprise = Emotion_Surprise,
    tiredness = Emotion_Tiredness,
    onEmotion = On_Emotion,
    onDanger = On_Danger,
)

val LocalStaticEmotionColors = staticCompositionLocalOf { DefaultMoodlyEmotionColors }
