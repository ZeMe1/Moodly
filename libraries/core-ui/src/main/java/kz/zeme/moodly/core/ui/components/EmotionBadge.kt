package kz.zeme.moodly.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.medium
import kz.zeme.moodly.core.res.theme.primary

@Composable
fun EmotionBadge(
    title: String,
    dotColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MoodlyTheme.colors.buttonSecondary)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor),
        )

        Text(
            text = title,
            style = MoodlyTheme.typography.bodySmall.medium().primary(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF16171B)
@Composable
internal fun EmotionBadgePreview() {
    EmotionBadge(
        modifier = Modifier.padding(8.dp),
        title = "Злость",
        dotColor = MoodlyTheme.emotionColors.anger,
    )
}
