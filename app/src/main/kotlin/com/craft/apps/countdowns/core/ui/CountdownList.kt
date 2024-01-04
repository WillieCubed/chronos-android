package com.craft.apps.countdowns.core.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.craft.apps.countdowns.core.data.repository.Countdown
import com.craft.apps.countdowns.core.data.repository.formatted

/**
 * A list of countdowns.
 */
@Composable
fun CountdownList(countdowns: List<Countdown>) {
    LazyColumn {
        items(countdowns) {
            CountdownListItem(countdown = it)
        }
    }
}

/**
 * A single countdown.
 */
@Composable
fun CountdownListItem(countdown: Countdown) {
    ListItem(
        headlineContent = { Text(countdown.label) },
        supportingContent = { Text(countdown.timestamp.formatted()) },
    )
}

@Preview
@Composable
fun CountdownListPreview() {
    CountdownList(countdowns = testCountdowns)
}