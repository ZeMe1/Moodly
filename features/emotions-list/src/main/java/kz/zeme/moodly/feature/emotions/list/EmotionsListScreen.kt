package kz.zeme.moodly.feature.emotions.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.moodly.core.error.BaseError
import kz.zeme.moodly.core.error.UnexpectedError
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.Note
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.align
import kz.zeme.moodly.core.res.theme.bold
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.res.theme.secondary
import kz.zeme.moodly.core.ui.components.DefaultErrorScreen
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic
import kz.zeme.moodly.core.ui.extensions.shimmer

@Composable
internal fun EmotionsListScreen(
    state: State<ViewState<EmotionsListState>?>,
    controller: EmotionsListController,
) {
    EmotionsListScreen(
        state = state.value,
        controller = controller,
    )
}

@Composable
private fun DefaultErrorView(
    modifier: Modifier = Modifier,
    error: BaseError? = null,
    onRefresh: () -> Unit,
) {
    DefaultErrorScreen(
        modifier = modifier,
        errorMessage = error?.message ?: stringResource(R.string.shared_something_went_wrong),
        onRefresh = onRefresh,
    )
}

@Composable
private fun EmotionsListScreen(
    state: ViewState<EmotionsListState>?,
    controller: EmotionsListController,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    when (state) {
        null,
        is ViewState.Loading.Skeleton -> EmotionsListShimmerView(
            modifier = Modifier
                .fillMaxSize()
                .background(MoodlyTheme.colors.mainBg)
                .statusBarsPadding(),
        )

        is ViewState.Error -> DefaultErrorView(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            error = state.error,
            onRefresh = controller::onRefresh,
        )

        is ViewState.Loading.ProgressBar,
        is ViewState.Data -> state.data?.let {
            EmotionsListContent(
                data = it,
                controller = controller,
                lazyListState = lazyListState,
            )
        } ?: EmotionsListShimmerView(
            modifier = Modifier
                .fillMaxSize()
                .background(MoodlyTheme.colors.mainBg)
                .statusBarsPadding(),
        )
    }
}

@Composable
private fun EmotionsListContent(
    data: EmotionsListState,
    controller: EmotionsListController,
    lazyListState: LazyListState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MoodlyTheme.colors.mainBg)
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickableHaptic(onClick = controller::onBackClick),
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = MoodlyTheme.colors.textPrimary,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(data.emotion.color()),
            )

            Text(
                text = stringResource(data.emotion.titleRes()),
                style = MoodlyTheme.typography.titleLarge.primary(),
            )
        }

        if (data.notes.isEmpty()) {
            EmotionsListEmptyView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = 16.dp,
                ),
            ) {
                emotionsListNotesSection(
                    notes = data.notes,
                    onNoteClick = controller::onNoteClick,
                )
            }
        }
    }
}

private fun LazyListScope.emotionsListNotesSection(
    notes: ImmutableList<Note>,
    onNoteClick: (Long) -> Unit,
) {
    items(items = notes) { note ->
        EmotionNoteCardView(
            note = note,
            onClick = { onNoteClick(note.id) },
        )
    }
}

@Composable
private fun EmotionNoteCardView(
    note: Note,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MoodlyTheme.colors.basicDark)
            .clickableHaptic(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = note.formattedDate,
            style = MoodlyTheme.typography.bodySmall.secondary(),
        )

        Text(
            text = note.text,
            style = MoodlyTheme.typography.bodyMedium.primary(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun EmotionsListEmptyView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 6.dp,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Text(
            text = stringResource(R.string.emotions_list_empty_title),
            style = MoodlyTheme.typography.titleSmall.bold().primary(),
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(R.string.emotions_list_empty_subtitle),
            style = MoodlyTheme.typography.bodyMedium.secondary().align(TextAlign.Center),
        )
    }
}

@Composable
private fun EmotionsListShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )

            Spacer(
                modifier = Modifier
                    .width(120.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
            ),
        ) {
            emotionsListShimmerSection()
        }
    }
}

private fun LazyListScope.emotionsListShimmerSection() {
    items(5) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .height(76.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MoodlyTheme.colors.mainShimmer)
                .shimmer(),
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

internal val emotionsListControllerStub = object : EmotionsListController {
    override fun onStart() = Unit
    override fun onRefresh() = Unit
    override fun onBackClick() = Unit
    override fun onNoteClick(noteId: Long) = Unit
}

internal val emotionsListStateStub = EmotionsListState(
    emotion = Emotion.Anger,
    notes = persistentListOf(
        Note(
            id = 1L,
            emotion = Emotion.Anger,
            text = "Опять перенесли дедлайн на завтра, а предупредили за час до конца рабочего дня. Хотелось всё бросить и уйти.",
            formattedDate = "27 ноября 2026, 18:40",
        ),
        Note(
            id = 2L,
            emotion = Emotion.Anger,
            text = "Застрял в пробке на 40 минут и опоздал на важную встречу. Весь день потом шёл наперекосяк.",
            formattedDate = "24 ноября 2026, 09:10",
        ),
        Note(
            id = 3L,
            emotion = Emotion.Anger,
            text = "Сосед снова сверлил весь вечер, хотя обещал закончить ремонт ещё на прошлой неделе.",
            formattedDate = "20 ноября 2026, 21:30",
        ),
    ),
)

private data class EmotionsListScreenPreviewParameter(
    val state: ViewState<EmotionsListState>,
    val lazyListState: LazyListState = LazyListState(),
)

private class EmotionsListScreenPreviewParameterProvider :
    PreviewParameterProvider<EmotionsListScreenPreviewParameter> {

    override val values: Sequence<EmotionsListScreenPreviewParameter> = sequenceOf(
        EmotionsListScreenPreviewParameter(
            state = ViewState.Data(emotionsListStateStub),
        ),
        EmotionsListScreenPreviewParameter(
            state = ViewState.Data(EmotionsListState()),
        ),
        EmotionsListScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        EmotionsListScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun EmotionsListScreenPreview(
    @PreviewParameter(EmotionsListScreenPreviewParameterProvider::class)
    parameter: EmotionsListScreenPreviewParameter,
) {
    EmotionsListScreen(
        state = parameter.state,
        controller = emotionsListControllerStub,
        lazyListState = parameter.lazyListState,
    )
}
