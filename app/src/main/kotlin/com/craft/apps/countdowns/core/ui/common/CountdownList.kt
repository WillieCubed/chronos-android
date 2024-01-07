package com.craft.apps.countdowns.core.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.craft.apps.countdowns.core.data.repository.Countdown
import com.craft.apps.countdowns.core.data.repository.formatted
import com.craft.apps.countdowns.core.ui.home.testCountdowns

/**
 * A list of countdowns.
 */
@Composable
fun CountdownList(
    countdowns: List<Countdown>,
    onCountdownSelected: (countdownId: Int) -> Unit,
    onCountdownDeletion: (countdownId: Int) -> Unit,
    onPinToLauncher: (countdownId: Int) -> Unit,
) {
    LazyColumn {
        items(countdowns) {
            CountdownListItem(
                countdown = it,
                onCountdownSelected = onCountdownSelected,
                onCountdownDeletion = onCountdownDeletion,
                onPinToLauncher = onPinToLauncher,
            )
        }
    }
}

/**
 * A single countdown.
 */
@Composable
fun CountdownListItem(
    countdown: Countdown,
    onCountdownSelected: (countdownId: Int) -> Unit,
    onCountdownDeletion: (countdownId: Int) -> Unit,
    onPinToLauncher: (countdownId: Int) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier.clickable { onCountdownSelected(countdown.id) },
        headlineContent = { Text(countdown.label) },
        supportingContent = { Text(countdown.timestamp.formatted()) },
        trailingContent = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Options",
                )
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text("Pin to launcher") },
                    onClick = { onPinToLauncher(countdown.id) },
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = { onCountdownDeletion(countdown.id) },
                )
            }
        })
}

@Preview
@Composable
fun CountdownListPreview() {
    CountdownList(countdowns = testCountdowns,
        onCountdownSelected = {},
        onCountdownDeletion = {},
        onPinToLauncher = {})
}