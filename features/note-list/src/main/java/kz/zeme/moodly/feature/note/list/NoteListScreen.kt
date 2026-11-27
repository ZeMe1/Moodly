package kz.zeme.moodly.feature.note.list

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.moodly.core.error.BaseError
import kz.zeme.moodly.core.error.UnexpectedError
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.align
import kz.zeme.moodly.core.res.theme.bold
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.res.theme.secondary
import kz.zeme.moodly.core.ui.components.DefaultErrorScreen
import kz.zeme.moodly.core.ui.components.EmotionBadge
import kz.zeme.moodly.core.ui.components.MoodlyBottomBar
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic
import kz.zeme.moodly.core.ui.extensions.shimmer

@Composable
internal fun NoteListScreen(
    state: State<ViewState<NoteListState>?>,
    controller: NoteListController,
) {
    NoteListScreen(
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
private fun NoteListScreen(
    state: ViewState<NoteListState>?,
    controller: NoteListController,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    when (state) {
        null,
        is ViewState.Loading.Skeleton -> NoteListShimmerView(
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
            NoteListContent(
                data = it,
                controller = controller,
                lazyListState = lazyListState,
            )
        } ?: NoteListShimmerView(
            modifier = Modifier
                .fillMaxSize()
                .background(MoodlyTheme.colors.mainBg)
                .statusBarsPadding(),
        )
    }
}

@Composable
private fun NoteListContent(
    data: NoteListState,
    controller: NoteListController,
    lazyListState: LazyListState,
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
            text = stringResource(R.string.note_list_title),
            style = MoodlyTheme.typography.titleLarge.primary(),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            if (data.dayGroups.isEmpty()) {
                NoteListEmptyView(
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 84.dp,
                    ),
                ) {
                    data.dayGroups.forEachIndexed { index, group ->
                        noteDayGroupSection(
                            group = group,
                            isFirst = index == 0,
                            onNoteClick = controller::onNoteClick,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MoodlyTheme.colors.buttonPrimary)
                    .clickableHaptic(onClick = controller::onCreateNoteClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.note_create_title),
                    tint = MoodlyTheme.colors.textPrimary,
                )
            }
        }

        MoodlyBottomBar(
            selectedTab = MoodlyBottomTab.Notes,
            onTabClick = controller::onTabClick,
        )
    }
}

@Composable
private fun NoteListEmptyView(
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
            text = stringResource(R.string.note_list_empty_title),
            style = MoodlyTheme.typography.titleSmall.bold().primary(),
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(R.string.note_list_empty_subtitle),
            style = MoodlyTheme.typography.bodyMedium.secondary().align(TextAlign.Center),
        )
    }
}

private fun LazyListScope.noteDayGroupSection(
    group: NoteDayGroup,
    isFirst: Boolean,
    onNoteClick: (Long) -> Unit,
) {
    item {
        Text(
            modifier = Modifier.padding(
                start = 4.dp,
                end = 4.dp,
                top = if (isFirst) 6.dp else 16.dp,
                bottom = 8.dp,
            ),
            text = group.dayTitle,
            style = MoodlyTheme.typography.bodySmall.secondary(),
        )
    }

    items(items = group.notes) { item ->
        NoteCardView(
            item = item,
            onClick = { onNoteClick(item.id) },
        )
    }
}

@Composable
private fun NoteCardView(
    item: NoteCardItem,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EmotionBadge(
                title = stringResource(item.emotion.titleRes()),
                dotColor = item.emotion.color(),
            )

            Text(
                text = item.timeText,
                style = MoodlyTheme.typography.bodySmall.secondary(),
            )
        }

        Text(
            text = item.text,
            style = MoodlyTheme.typography.bodyMedium.primary(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun NoteListShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 14.dp)
                .width(120.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MoodlyTheme.colors.mainShimmer)
                .shimmer(),
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            noteListShimmerSection()
        }

        MoodlyBottomBar(
            selectedTab = MoodlyBottomTab.Notes,
            onTabClick = {},
        )
    }
}

private fun LazyListScope.noteListShimmerSection() {
    item {
        Spacer(
            modifier = Modifier
                .padding(start = 4.dp, top = 6.dp, bottom = 8.dp)
                .width(72.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MoodlyTheme.colors.mainShimmer)
                .shimmer(),
        )
    }

    items(4) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .height(96.dp)
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

internal val noteListControllerStub = object : NoteListController {
    override fun onStart() = Unit
    override fun onRefresh() = Unit
    override fun onCreateNoteClick() = Unit
    override fun onNoteClick(noteId: Long) = Unit
    override fun onTabClick(tab: MoodlyBottomTab) = Unit
}

internal val noteListStateStub = NoteListState(
    dayGroups = persistentListOf(
        NoteDayGroup(
            dayTitle = "Сегодня",
            notes = persistentListOf(
                NoteCardItem(
                    id = 1L,
                    emotion = Emotion.Anger,
                    timeText = "18:40",
                    text = "Опять перенесли дедлайн на завтра, а предупредили за час до конца рабочего дня.",
                ),
                NoteCardItem(
                    id = 2L,
                    emotion = Emotion.Joy,
                    timeText = "12:15",
                    text = "Пообедал с другом в новом кафе, долго смеялись. Отличный перерыв посреди дня.",
                ),
            ),
        ),
        NoteDayGroup(
            dayTitle = "Вчера",
            notes = persistentListOf(
                NoteCardItem(
                    id = 3L,
                    emotion = Emotion.Calm,
                    timeText = "08:30",
                    text = "Утро без спешки: кофе, тишина, немного почитал.",
                ),
            ),
        ),
    ),
)

private data class NoteListScreenPreviewParameter(
    val state: ViewState<NoteListState>,
    val lazyListState: LazyListState = LazyListState(),
)

private class NoteListScreenPreviewParameterProvider :
    PreviewParameterProvider<NoteListScreenPreviewParameter> {

    override val values: Sequence<NoteListScreenPreviewParameter> = sequenceOf(
        NoteListScreenPreviewParameter(
            state = ViewState.Data(noteListStateStub),
        ),
        NoteListScreenPreviewParameter(
            state = ViewState.Data(NoteListState()),
        ),
        NoteListScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        NoteListScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteListScreenPreview(
    @PreviewParameter(NoteListScreenPreviewParameterProvider::class)
    parameter: NoteListScreenPreviewParameter,
) {
    NoteListScreen(
        state = parameter.state,
        controller = noteListControllerStub,
        lazyListState = parameter.lazyListState,
    )
}
