package kz.zeme.moodly.feature.note.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import kz.zeme.moodly.core.error.BaseError
import kz.zeme.moodly.core.error.UnexpectedError
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.Note
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.res.theme.secondary
import kz.zeme.moodly.core.ui.components.DefaultErrorScreen
import kz.zeme.moodly.core.ui.components.EmotionBadge
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic
import kz.zeme.moodly.core.ui.extensions.shimmer

@Composable
internal fun NoteDetailScreen(
    state: State<ViewState<Note>?>,
    controller: NoteDetailController,
) {
    NoteDetailScreen(
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
private fun NoteDetailScreen(
    state: ViewState<Note>?,
    controller: NoteDetailController,
    scrollState: ScrollState = rememberScrollState(),
) {
    when (state) {
        null,
        is ViewState.Loading.Skeleton -> NoteDetailShimmerView(
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
            NoteDetailContent(
                data = it,
                controller = controller,
                scrollState = scrollState,
            )
        } ?: NoteDetailShimmerView(
            modifier = Modifier
                .fillMaxSize()
                .background(MoodlyTheme.colors.mainBg)
                .statusBarsPadding(),
        )
    }
}

@Composable
private fun NoteDetailContent(
    data: Note,
    controller: NoteDetailController,
    scrollState: ScrollState,
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

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickableHaptic(onClick = controller::onEditClick),
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = stringResource(R.string.note_edit_title),
                tint = MoodlyTheme.colors.textPrimary,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickableHaptic(onClick = controller::onDeleteClick),
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.note_delete_dialog_confirm),
                tint = MoodlyTheme.colors.textPrimary,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .navigationBarsPadding(),
        ) {
            EmotionBadge(
                title = stringResource(data.emotion.titleRes()),
                dotColor = data.emotion.color(),
            )

            Text(
                modifier = Modifier.padding(vertical = 14.dp),
                text = data.formattedDate,
                style = MoodlyTheme.typography.bodySmall.secondary(),
            )

            Text(
                text = data.text,
                style = MoodlyTheme.typography.bodyLarge.primary(),
            )
        }
    }
}

@Composable
private fun NoteDetailShimmerView(
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

            Spacer(modifier = Modifier.weight(1f))

            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .width(96.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )

            Spacer(
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .width(140.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MoodlyTheme.colors.mainShimmer)
                    .shimmer(),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(4) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MoodlyTheme.colors.mainShimmer)
                            .shimmer(),
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MoodlyTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }
        }
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

internal val noteDetailControllerStub = object : NoteDetailController {
    override fun onStart() = Unit
    override fun onRefresh() = Unit
    override fun onBackClick() = Unit
    override fun onEditClick() = Unit
    override fun onDeleteClick() = Unit
}

internal val noteDetailStub = Note(
    id = 1L,
    emotion = Emotion.Anger,
    text = "Опять перенесли дедлайн на завтра, а предупредили за час до конца рабочего дня. Хотелось всё бросить и уйти.\n\nСдержался, но весь вечер был на взводе. Надо будет спокойно поговорить с руководителем о сроках.",
    createdAt = 1795786800000L,
    formattedDate = "27 ноября 2026, 18:40",
)

internal val noteDetailEmptyStub = Note()

private data class NoteDetailScreenPreviewParameter(
    val state: ViewState<Note>,
)

private class NoteDetailScreenPreviewParameterProvider :
    PreviewParameterProvider<NoteDetailScreenPreviewParameter> {

    override val values: Sequence<NoteDetailScreenPreviewParameter> = sequenceOf(
        NoteDetailScreenPreviewParameter(
            state = ViewState.Data(noteDetailStub),
        ),
        NoteDetailScreenPreviewParameter(
            state = ViewState.Data(noteDetailEmptyStub),
        ),
        NoteDetailScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        NoteDetailScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteDetailScreenPreview(
    @PreviewParameter(NoteDetailScreenPreviewParameterProvider::class)
    parameter: NoteDetailScreenPreviewParameter,
) {
    NoteDetailScreen(
        state = parameter.state,
        controller = noteDetailControllerStub,
    )
}
