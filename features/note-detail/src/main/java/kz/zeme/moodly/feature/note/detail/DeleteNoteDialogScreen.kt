package kz.zeme.moodly.feature.note.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.res.theme.MoodlyTheme
import kz.zeme.moodly.core.res.theme.bold
import kz.zeme.moodly.core.res.theme.primary
import kz.zeme.moodly.core.res.theme.secondary
import kz.zeme.moodly.core.ui.extensions.compose.clickableHaptic

@Composable
internal fun DeleteNoteDialogScreen(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MoodlyTheme.colors.basicDark)
            .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.note_delete_dialog_title),
            style = MoodlyTheme.typography.titleSmall.bold().primary(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.note_delete_dialog_message),
            style = MoodlyTheme.typography.bodyMedium.secondary(),
        )

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MoodlyTheme.colors.buttonSecondary)
                    .clickableHaptic(onClick = onCancelClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.note_delete_dialog_cancel),
                    style = MoodlyTheme.typography.labelLarge.bold().primary(),
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MoodlyTheme.colors.basicError)
                    .clickableHaptic(onClick = onConfirmClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.note_delete_dialog_confirm),
                    style = MoodlyTheme.typography.labelLarge.bold(),
                    color = MoodlyTheme.emotionColors.onDanger,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
private fun DeleteNoteDialogScreenPreview() {
    DeleteNoteDialogScreen(
        onCancelClick = {},
        onConfirmClick = {},
    )
}
