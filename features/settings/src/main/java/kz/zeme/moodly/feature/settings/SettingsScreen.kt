package kz.zeme.moodly.feature.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.ui.components.MoodlyBottomBar
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic

@Composable
internal fun SettingsScreen(
    controller: SettingsController,
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
            text = stringResource(R.string.settings_title),
            style = MoodlyTheme.typography.titleLarge.primary(),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SettingsItemRow(
                iconRes = R.drawable.ic_theme_moon,
                title = stringResource(R.string.settings_theme),
                onClick = controller::onThemeClick,
            )

            SettingsItemRow(
                iconRes = R.drawable.ic_language_globe,
                title = stringResource(R.string.settings_language),
                onClick = controller::onLanguageClick,
            )
        }

        MoodlyBottomBar(
            selectedTab = MoodlyBottomTab.Settings,
            onTabClick = controller::onTabClick,
        )
    }
}

@Composable
private fun SettingsItemRow(
    @DrawableRes iconRes: Int,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MoodlyTheme.colors.basicDark)
            .clickableHaptic(onClick = onClick)
            .padding(start = 16.dp, end = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MoodlyTheme.colors.textPrimary,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MoodlyTheme.typography.bodyLarge.primary(),
        )

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MoodlyTheme.colors.textSecondary,
        )
    }
}

private val settingsControllerStub = object : SettingsController {

    override fun onThemeClick() = Unit

    override fun onLanguageClick() = Unit

    override fun onTabClick(tab: MoodlyBottomTab) = Unit
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(controller = settingsControllerStub)
}
