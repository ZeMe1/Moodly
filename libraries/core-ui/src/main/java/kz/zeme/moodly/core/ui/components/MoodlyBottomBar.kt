package kz.zeme.moodly.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic

enum class MoodlyBottomTab(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
) {
    Notes(
        iconRes = R.drawable.ic_notes,
        titleRes = R.string.bottom_nav_notes,
    ),
    Emotions(
        iconRes = R.drawable.ic_emotions_grid,
        titleRes = R.string.bottom_nav_emotions,
    ),
    Settings(
        iconRes = R.drawable.ic_settings,
        titleRes = R.string.bottom_nav_settings,
    ),
}

@Composable
fun MoodlyBottomBar(
    selectedTab: MoodlyBottomTab,
    onTabClick: (MoodlyBottomTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MoodlyTheme.colors.mainBg)
            .navigationBarsPadding(),
    ) {
        HorizontalDivider(
            color = MoodlyTheme.colors.buttonDisabled,
            thickness = 1.dp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MoodlyBottomTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val contentColor =
                    if (isSelected) MoodlyTheme.colors.textPrimary
                    else MoodlyTheme.colors.textSecondary

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(62.dp)
                        .clickableHaptic { onTabClick(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        space = 4.dp,
                        alignment = Alignment.CenterVertically,
                    ),
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(tab.iconRes),
                        contentDescription = stringResource(tab.titleRes),
                        tint = contentColor,
                    )

                    Text(
                        text = stringResource(tab.titleRes),
                        style = MoodlyTheme.typography.labelSmall,
                        color = contentColor,
                    )
                }
            }
        }
    }
}

internal class MoodlyBottomBarPreviewParameterProvider :
    PreviewParameterProvider<MoodlyBottomTab> {
    override val values: Sequence<MoodlyBottomTab> = MoodlyBottomTab.entries.asSequence()
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
internal fun MoodlyBottomBarPreview(
    @PreviewParameter(MoodlyBottomBarPreviewParameterProvider::class)
    selectedTab: MoodlyBottomTab,
) {
    MoodlyBottomBar(
        selectedTab = selectedTab,
        onTabClick = {},
    )
}
