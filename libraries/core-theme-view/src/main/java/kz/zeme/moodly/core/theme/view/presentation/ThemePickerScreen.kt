package kz.zeme.moodly.core.theme.view.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kz.zeme.moodly.core.error.BaseError
import kz.zeme.moodly.core.error.UnexpectedError
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.theme.domain.AppTheme
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode.Dark
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode.Light
import kz.zeme.moodly.core.theme.domain.AppTheme.ThemeMode.System
import kz.zeme.moodly.core.ui.components.DefaultErrorScreen
import kz.zeme.moodly.core.ui.compose.views.ModalBottomSheet
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic
import kz.zeme.moodly.core.ui.extensions.shimmer

@Composable
fun ThemePickerScreen(
    state: State<ViewState<List<AppTheme>>?>,
    controller: ThemePickerController,
) {
    ThemePickerScreen(
        viewState = state.value,
        controller = controller,
    )
}

@Composable
private fun ThemePickerScreen(
    viewState: ViewState<List<AppTheme>>?,
    controller: ThemePickerController,
) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.theme_picker_title),
            style = MoodlyTheme.typography.titleMedium.primary(),
        )

        when (viewState) {
            null,
            is ViewState.Loading.Skeleton -> ThemePickerShimmerView()

            is ViewState.Error -> DefaultErrorView(
                error = viewState.error,
                onRefresh = controller::onStart,
            )

            is ViewState.Loading.ProgressBar,
            is ViewState.Data -> viewState.data?.let {
                ThemeListContent(
                    themes = it,
                    controller = controller,
                )
            } ?: ThemePickerShimmerView()
        }
    }
}

@Composable
private fun DefaultErrorView(
    modifier: Modifier = Modifier,
    error: BaseError? = null,
    onRefresh: () -> Unit,
) {
    DefaultErrorScreen(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        errorMessage = error?.message ?: stringResource(R.string.shared_something_went_wrong),
        onRefresh = onRefresh,
        backgroundColor = MoodlyTheme.colors.basicDark,
    )
}

@Composable
private fun ThemePickerShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MoodlyTheme.colors.mainShimmer)
                        .shimmer(),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MoodlyTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }
        }
    }
}

@Composable
private fun ThemeListContent(
    themes: List<AppTheme>,
    controller: ThemePickerController,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    ) {
        items(items = themes) { theme ->
            ThemeItemRow(
                theme = theme,
                onClick = { controller.onThemeClick(theme) },
            )
            if (themes.lastOrNull() != theme) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MoodlyTheme.colors.buttonDisabled.copy(alpha = 0.5f),
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
private fun ThemeItemRow(
    theme: AppTheme,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableHaptic(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
                .alpha(if (theme.isSelected) 1f else 0f),
            painter = painterResource(R.drawable.ic_check_mark),
            contentDescription = null,
            tint = MoodlyTheme.colors.textPrimary,
        )

        val titleRes = when (theme.mode) {
            Light -> R.string.theme_picker_light
            Dark -> R.string.theme_picker_dark
            System -> R.string.theme_picker_system
        }

        Text(
            text = stringResource(titleRes),
            style = MoodlyTheme.typography.bodyLarge.primary(),
            textAlign = TextAlign.Start,
        )
    }
}

private val themePickerControllerStub = object : ThemePickerController {
    override fun onThemeClick(theme: AppTheme) = Unit
    override fun onClose() = Unit
    override fun onStart() = Unit
}

private val themeListStub = listOf(
    AppTheme(isSelected = false, mode = Light),
    AppTheme(isSelected = true, mode = Dark),
    AppTheme(isSelected = false, mode = System),
)

private data class ThemePickerScreenPreviewParameter(
    val state: ViewState<List<AppTheme>>,
)

private class ThemePickerScreenPreviewParameterProvider :
    PreviewParameterProvider<ThemePickerScreenPreviewParameter> {

    override val values: Sequence<ThemePickerScreenPreviewParameter> = sequenceOf(
        ThemePickerScreenPreviewParameter(
            state = ViewState.Data(themeListStub),
        ),
        ThemePickerScreenPreviewParameter(
            state = ViewState.Data(emptyList()),
        ),
        ThemePickerScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        ThemePickerScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF010101,
)
@Composable
private fun ThemePickerScreenPreview(
    @PreviewParameter(ThemePickerScreenPreviewParameterProvider::class)
    params: ThemePickerScreenPreviewParameter,
) {
    ThemePickerScreen(
        viewState = params.state,
        controller = themePickerControllerStub,
    )
}
