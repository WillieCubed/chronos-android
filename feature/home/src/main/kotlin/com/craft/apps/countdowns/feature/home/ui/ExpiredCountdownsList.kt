package com.craft.apps.countdowns.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.feature.home.R
import com.craft.apps.countdowns.ui.common.CountdownList
import com.craft.apps.countdowns.ui.common.IconLabel
import com.craft.apps.countdowns.ui.theme.Spacing
import com.craft.apps.countdowns.ui.util.testCountdowns

@Composable
internal fun ExpiredCountdownsList(
    countdowns: List<Countdown>,
    onCountdownSelected: (countdownId: Int) -> Unit,
    onDeleteCountdown: (countdownId: Int) -> Unit,
    onPinCountdown: (countdownId: Int) -> Unit,
    onFeatureCountdown: (countdownId: Int) -> Unit,
) {
    Column(
        Modifier.padding(vertical = Spacing.md)
    ) {
        Row(
            Modifier
                .padding(horizontal = Spacing.lg)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconLabel(
                icon = Icons.Default.HourglassEmpty,
                contentDescription = stringResource(R.string.content_description_expired_countdowns),
                label = stringResource(R.string.list_label_expired_countdowns),
            )
        }
        CountdownList(
            countdowns = countdowns,
            onCountdownSelected = onCountdownSelected,
            onCountdownDeletion = onDeleteCountdown,
            onPinToLauncher = onPinCountdown,
            onMarkFeatured = onFeatureCountdown,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpiredCountdownsListPreview() {
    ExpiredCountdownsList(
        countdowns = testCountdowns,
        onCountdownSelected = {},
        onDeleteCountdown = {},
        onPinCountdown = {},
        onFeatureCountdown = {},
    )
}