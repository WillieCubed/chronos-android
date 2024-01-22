package com.craft.apps.countdowns.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.core.model.formatted
import com.craft.apps.countdowns.feature.home.R
import com.craft.apps.countdowns.ui.common.IconLabel
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing
import com.craft.apps.countdowns.ui.util.testCountdowns
import com.craft.apps.countdowns.util.hoursUntilNow

@Composable
internal fun FeaturedCountdownList(
    countdowns: List<Countdown>,
    onCountdownSelected: (countdownId: Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            Modifier
                .padding(horizontal = Spacing.lg)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // TODO: Swap out with actual starred icon
            IconLabel(
                icon = Icons.Default.Favorite,
                contentDescription = stringResource(R.string.content_description_starred_countdowns),
                label = stringResource(R.string.list_label_starred_countdowns),
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            contentPadding = PaddingValues(horizontal = Spacing.lg)
        ) {
            items(countdowns) {
                FeaturedCountdownItem(it, onCountdownSelected)
            }
        }
    }
}

@Composable
internal fun FeaturedCountdownItem(
    countdown: Countdown,
    onCountdownSelected: (countdownId: Int) -> Unit,
) {
    Card(modifier = Modifier
        .height(328.dp)
        .width(240.dp)
        .clickable {
            onCountdownSelected(countdown.id)
        }) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.Cyan),
                verticalArrangement = Arrangement.spacedBy(
                    Spacing.md, Alignment.Top
                ),
                horizontalAlignment = Alignment.Start,
            ) {
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .padding(Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                // TODO: Choose best time for display automatically
                val timeUntil = countdown.expiration.hoursUntilNow()
                Text(countdown.label, style = ChronosTypography.labelLarge)
                Text(text = "$timeUntil hours", style = ChronosTypography.displayMedium)
                Text(
                    text = countdown.expiration.formatted(),
                    style = ChronosTypography.labelLarge
                )
            }
        }
    }
}

@Preview(name = "Populated List", showBackground = true)
@Composable
private fun FeaturedCountdownsListPreview() {
    FeaturedCountdownList(
        countdowns = testCountdowns,
        onCountdownSelected = {},
    )
}