@file:OptIn(ExperimentalFoundationApi::class)

package com.craft.apps.countdowns.ui.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.core.ui.R
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.util.formattedLongDate
import com.craft.apps.countdowns.ui.util.formattedShortTime
import com.craft.apps.countdowns.ui.util.testCountdowns
import kotlin.math.abs

/**
 * A generic list of countdowns.
 */
@Composable
fun CountdownList(
    countdowns: List<Countdown>,
    onCountdownSelected: (countdownId: Int) -> Unit,
    onCountdownDeletion: (countdownId: Int) -> Unit,
    onMarkFeatured: (countdownId: Int) -> Unit,
    onPinToLauncher: (countdownId: Int) -> Unit,
) {
    LazyColumn {
        items(countdowns) {
            CountdownListItem(
                countdown = it,
                onCountdownSelected = onCountdownSelected,
                onCountdownDeletion = onCountdownDeletion,
                onPinToLauncher = onPinToLauncher,
                onMarkFeatured = onMarkFeatured,
            )
        }
    }
}

private enum class DragValue { Start, Center, End }

/**
 * A single countdown.
 */
@Composable
private fun CountdownListItem(
    countdown: Countdown,
    onCountdownSelected: (countdownId: Int) -> Unit,
    onCountdownDeletion: (countdownId: Int) -> Unit,
    onMarkFeatured: (countdownId: Int) -> Unit,
    onPinToLauncher: (countdownId: Int) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val anchors = DraggableAnchors {
        DragValue.Start at -128f
        DragValue.Center at 0f
        DragValue.End at 128f
    }

    fun positionalThresholdFunction(totalDistance: Float): Float {
        return abs(totalDistance - 96f)
    }

    val draggableState = remember {
        AnchoredDraggableState(
            DragValue.Center, anchors = anchors,
            ::positionalThresholdFunction,
            { 128f },
            tween(),
        )
    }
    Box(
        Modifier.offset { IntOffset(x = draggableState.requireOffset().toInt(), y = 0) }
    ) {

        ListItem(
            modifier = Modifier
                .clickable { onCountdownSelected(countdown.id) }
                .anchoredDraggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                ),
            headlineContent = { Text(countdown.label, style = ChronosTypography.titleLarge) },
            supportingContent = {
                Text(
                    stringResource(
                        R.string.formatted_date_time,
                        countdown.expiration.formattedLongDate(),
                        countdown.expiration.formattedShortTime(),
                    ),
                    style = ChronosTypography.titleMedium,
                )
            },
            trailingContent = {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Options",
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        leadingIcon = { Icon(Icons.Default.Delete, "Delete countdown") },
                        onClick = {
                            onCountdownDeletion(countdown.id)
                        },
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Pin to launcher") },
                        onClick = {
                            onPinToLauncher(countdown.id)
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Add to favorites") },
                        onClick = {
                            onMarkFeatured(countdown.id)
                        },
                    )
                }
            })
    }
}

@Preview(showBackground = true, name = "Countdowns List")
@Composable
fun CountdownListPreview() {
    CountdownList(
        countdowns = testCountdowns,
        onCountdownSelected = {},
        onCountdownDeletion = {},
        onPinToLauncher = {},
        onMarkFeatured = {},
    )
}