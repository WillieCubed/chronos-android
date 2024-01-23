package com.craft.apps.countdowns.feature.details.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing
import com.craft.apps.countdowns.ui.util.CountdownDisplayStyle
import com.craft.apps.countdowns.ui.util.testCountdowns
import com.craft.apps.countdowns.util.secondsUntilNow
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant

/**
 * A countdown timer that displays the time remaining until the given [expirationTime].
 *
 * This countdown timer displays differently depending on the given [displayStyle].
 */
@Composable
fun LargeCountdownTimer(
    expirationTime: Instant,
    displayStyle: CountdownDisplayStyle,
    modifier: Modifier = Modifier,
) {
    var timeLeftSeconds by remember { mutableLongStateOf(expirationTime.secondsUntilNow()) }
    LaunchedEffect(key1 = timeLeftSeconds) {
        while (true) { // TODO: Make sure this doesn't brick a device
            delay(1_000)
            timeLeftSeconds -= 1
        }
    }

    // TODO: Change display style
    when (displayStyle) {
        CountdownDisplayStyle.DYNAMIC -> {}

        CountdownDisplayStyle.LEAST_SIGNIFICANT -> {}

        CountdownDisplayStyle.SHORT_TERM -> {}
    }

    val units = "seconds"

    Column(
        modifier = modifier
            .defaultMinSize(minWidth = 360.dp, minHeight = 360.dp)
            .padding(Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = timeLeftSeconds.toString(),
            style = ChronosTypography.displayLarge.copy(
                fontSize = 96.sp,
                lineHeight = 108.sp,
            ),
        )
        Text(
            text = units,
            style = ChronosTypography.displayLarge.copy(
                fontSize = 72.sp,
                lineHeight = 81.sp,
            ),
        )
    }
}

@Preview(name = "Active Countdown", showBackground = true)
@Composable
private fun LargeCountdownTimerPreview() {
    LargeCountdownTimer(testCountdowns[1].expiration, CountdownDisplayStyle.DYNAMIC)
}

@Preview(name = "Expired Countdown", showBackground = true)
@Composable
private fun LargeCountdownTimerExpiredPreview() {
    LargeCountdownTimer(testCountdowns[0].expiration, CountdownDisplayStyle.DYNAMIC)
}