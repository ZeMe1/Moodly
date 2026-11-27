package kz.zeme.moodly.feature.emotions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.bold
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.ui.components.MoodlyBottomBar
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic

@Composable
internal fun EmotionsScreen(
    controller: EmotionsController,
    lazyGridState: LazyGridState = rememberLazyGridState(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MoodlyTheme.colors.mainBg)
            .statusBarsPadding(),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 14.dp),
            text = stringResource(R.string.emotions_title),
            style = MoodlyTheme.typography.titleLarge.primary(),
        )

        LazyVerticalGrid(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            columns = GridCells.Fixed(2),
            state = lazyGridState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            emotionsGridSection(
                onEmotionClick = controller::onEmotionClick,
            )
        }

        MoodlyBottomBar(
            selectedTab = MoodlyBottomTab.Emotions,
            onTabClick = controller::onTabClick,
        )
    }
}

private fun LazyGridScope.emotionsGridSection(
    onEmotionClick: (Emotion) -> Unit,
) {
    items(items = Emotion.entries) { emotion ->
        EmotionCategoryCardView(
            emotion = emotion,
            onClick = { onEmotionClick(emotion) },
        )
    }
}

@Composable
private fun EmotionCategoryCardView(
    emotion: Emotion,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(emotion.color())
            .clickableHaptic(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        Text(
            text = stringResource(emotion.titleRes()),
            style = MoodlyTheme.typography.bodyLarge.bold(),
            color = MoodlyTheme.emotionColors.onEmotion,
        )
    }
}

private fun Emotion.titleRes(): Int = when (this) {
    Emotion.Anger -> R.string.emotion_anger
    Emotion.Joy -> R.string.emotion_joy
    Emotion.Sadness -> R.string.emotion_sadness
    Emotion.Calm -> R.string.emotion_calm
    Emotion.Fear -> R.string.emotion_fear
    Emotion.Anxiety -> R.string.emotion_anxiety
    Emotion.Surprise -> R.string.emotion_surprise
    Emotion.Tiredness -> R.string.emotion_tiredness
}

@Composable
private fun Emotion.color(): Color = when (this) {
    Emotion.Anger -> MoodlyTheme.emotionColors.anger
    Emotion.Joy -> MoodlyTheme.emotionColors.joy
    Emotion.Sadness -> MoodlyTheme.emotionColors.sadness
    Emotion.Calm -> MoodlyTheme.emotionColors.calm
    Emotion.Fear -> MoodlyTheme.emotionColors.fear
    Emotion.Anxiety -> MoodlyTheme.emotionColors.anxiety
    Emotion.Surprise -> MoodlyTheme.emotionColors.surprise
    Emotion.Tiredness -> MoodlyTheme.emotionColors.tiredness
}

private val emotionsControllerStub = object : EmotionsController {

    override fun onEmotionClick(emotion: Emotion) = Unit

    override fun onTabClick(tab: MoodlyBottomTab) = Unit
}

@Preview(showBackground = true)
@Composable
private fun EmotionsScreenPreview() {
    EmotionsScreen(controller = emotionsControllerStub)
}
