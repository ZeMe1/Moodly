package kz.zeme.moodly.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.bold
import kz.zeme.moodly.core.res.theme.medium
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic

@Composable
fun EmotionSelectorChip(
    title: String,
    emotionColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        if (isSelected) emotionColor
        else MoodlyTheme.colors.buttonSecondary

    val textColor =
        if (isSelected) MoodlyTheme.emotionColors.onEmotion
        else MoodlyTheme.colors.textSecondary

    val textStyle =
        if (isSelected) MoodlyTheme.typography.bodyMedium.bold()
        else MoodlyTheme.typography.bodyMedium.medium()

    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickableHaptic(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = textStyle,
            color = textColor,
        )
    }
}

internal class EmotionSelectorChipPreviewParameterProvider :
    PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
internal fun EmotionSelectorChipPreview(
    @PreviewParameter(EmotionSelectorChipPreviewParameterProvider::class)
    isSelected: Boolean,
) {
    EmotionSelectorChip(
        modifier = Modifier.padding(8.dp),
        title = "Радость",
        emotionColor = MoodlyTheme.emotionColors.joy,
        isSelected = isSelected,
        onClick = {},
    )
}
