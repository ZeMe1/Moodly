package kz.zeme.moodly.core.ui.extensions.compose

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

fun Modifier.clickableHaptic(
    enabled: Boolean = true,
    withIndication: Boolean = true,
    hapticFeedbackType: HapticFeedbackType = HapticFeedbackType.LongPress,
    onClick: () -> Unit,
): Modifier = composed {
    val haptic = LocalHapticFeedback.current

    clickable(
        enabled = enabled,
        indication = if (withIndication) LocalIndication.current else null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        haptic.performHapticFeedback(hapticFeedbackType)
        onClick()
    }
}

fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        enabled = enabled,
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick,
    )
}
