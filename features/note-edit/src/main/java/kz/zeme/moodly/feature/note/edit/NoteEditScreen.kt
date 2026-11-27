package kz.zeme.moodly.feature.note.edit

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import kz.zeme.moodly.core.error.BaseError
import kz.zeme.moodly.core.error.UnexpectedError
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.bold
import kz.zeme.moodly.core.res.theme.enabled
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.res.theme.secondary
import kz.zeme.moodly.core.ui.components.DefaultErrorScreen
import kz.zeme.moodly.core.ui.components.EmotionSelectorChip
import kz.zeme.moodly.core.ui.compose.views.TextField
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic
import kz.zeme.moodly.core.ui.extensions.shimmer

@Composable
internal fun NoteEditScreen(
    state: State<ViewState<NoteEditState>?>,
    controller: NoteEditController,
) {
    NoteEditScreen(
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
private fun NoteEditScreen(
    state: ViewState<NoteEditState>?,
    controller: NoteEditController,
    scrollState: ScrollState = rememberScrollState(),
) {
    when (state) {
        null,
        is ViewState.Loading.Skeleton -> NoteEditShimmerView(
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
            NoteEditContent(
                data = it,
                controller = controller,
                scrollState = scrollState,
            )
        } ?: NoteEditShimmerView(
            modifier = Modifier
                .fillMaxSize()
                .background(MoodlyTheme.colors.mainBg)
                .statusBarsPadding(),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteEditContent(
    data: NoteEditState,
    controller: NoteEditController,
    scrollState: ScrollState,
) {
    val isSaveEnabled = data.canSave && !data.isSaving

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

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 14.dp),
            text = stringResource(R.string.note_edit_title),
            style = MoodlyTheme.typography.titleLarge.primary(),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 10.dp),
                text = stringResource(R.string.note_edit_emotion_label),
                style = MoodlyTheme.typography.bodySmall.secondary(),
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                data.emotions.forEach { emotion ->
                    EmotionSelectorChip(
                        title = stringResource(emotion.titleRes()),
                        emotionColor = emotion.color(),
                        isSelected = emotion == data.selectedEmotion,
                        onClick = { controller.onEmotionSelect(emotion) },
                    )
                }
            }

            Text(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 20.dp, bottom = 10.dp),
                text = stringResource(R.string.note_edit_text_label),
                style = MoodlyTheme.typography.bodySmall.secondary(),
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                minHeight = 160.dp,
                text = data.text,
                hint = stringResource(R.string.note_edit_text_hint),
                containerColor = MoodlyTheme.colors.inputBg,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(14.dp),
                onValueChange = controller::onTextChange,
            )

            Text(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 10.dp),
                text = data.createdAtLabel,
                style = MoodlyTheme.typography.bodySmall.secondary(),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .imePadding()
                .navigationBarsPadding(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSaveEnabled) MoodlyTheme.colors.buttonPrimary
                        else MoodlyTheme.colors.buttonDisabled,
                    )
                    .clickableHaptic(
                        enabled = isSaveEnabled,
                        onClick = controller::onSaveClick,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.note_edit_save),
                    style = MoodlyTheme.typography.labelLarge.primary().bold().enabled(isSaveEnabled),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteEditShimmerView(
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

        Spacer(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 14.dp)
                .width(180.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MoodlyTheme.colors.mainShimmer)
                .shimmer(),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .padding(start = 4.dp, top = 4.dp, bottom = 10.dp)
                    .width(64.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(8) {
                    Spacer(
                        modifier = Modifier
                            .width(84.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MoodlyTheme.colors.mainShimmer)
                            .shimmer(),
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .padding(start = 4.dp, top = 20.dp, bottom = 10.dp)
                    .width(64.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )

            Spacer(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 10.dp)
                    .width(180.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding()
                .height(50.dp)
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

internal val noteEditControllerStub = object : NoteEditController {
    override fun onStart() = Unit
    override fun onRefresh() = Unit
    override fun onBackClick() = Unit
    override fun onEmotionSelect(emotion: Emotion) = Unit
    override fun onTextChange(text: String) = Unit
    override fun onSaveClick() = Unit
}

internal val noteEditStateStub = NoteEditState(
    emotions = Emotion.entries.toImmutableList(),
    selectedEmotion = Emotion.Anger,
    text = "Опять перенесли дедлайн на завтра, а предупредили за час до конца рабочего дня. Хотелось всё бросить и уйти.\n\nСдержался, но весь вечер был на взводе.",
    createdAtLabel = "Создано 27 ноября 2026, 18:40",
    canSave = true,
)

internal val noteEditEmptyStateStub = NoteEditState(
    emotions = Emotion.entries.toImmutableList(),
    selectedEmotion = Emotion.Anger,
    text = "",
    createdAtLabel = "Создано 27 ноября 2026, 18:40",
    canSave = false,
)

private data class NoteEditScreenPreviewParameter(
    val state: ViewState<NoteEditState>,
)

private class NoteEditScreenPreviewParameterProvider :
    PreviewParameterProvider<NoteEditScreenPreviewParameter> {

    override val values: Sequence<NoteEditScreenPreviewParameter> = sequenceOf(
        NoteEditScreenPreviewParameter(
            state = ViewState.Data(noteEditStateStub),
        ),
        NoteEditScreenPreviewParameter(
            state = ViewState.Data(noteEditEmptyStateStub),
        ),
        NoteEditScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        NoteEditScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteEditScreenPreview(
    @PreviewParameter(NoteEditScreenPreviewParameterProvider::class)
    parameter: NoteEditScreenPreviewParameter,
) {
    NoteEditScreen(
        state = parameter.state,
        controller = noteEditControllerStub,
    )
}
