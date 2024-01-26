package com.craft.apps.countdowns.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.core.ui.R
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing
import com.craft.apps.countdowns.ui.util.formattedLongDate
import com.craft.apps.countdowns.ui.util.formattedShortTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

// TODO: Turn this into a real object in data layer
data class UiEventData(
    val eventId: String,
    val title: String,
    val timestamp: Instant,
)

/**
 * A Composable button that displays linked event information.
 */
@Composable
fun EventLinkButton(
    onAddEvent: () -> Unit,
    onUnlinkEvent: (eventId: String) -> Unit,
    modifier: Modifier = Modifier,
    eventData: UiEventData? = null,
) {
    if (eventData != null) {
        LinkedEvent(eventData, onUnlinkEvent, modifier)
    } else {
        UnlinkedEvent(onAddEvent, modifier)
    }
}

@Composable
private fun UnlinkedEvent(onAddEvent: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable { onAddEvent() }
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(horizontal = Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.EditCalendar,
            contentDescription = null,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp),
            tint = MaterialTheme.colorScheme.surfaceTint,
        )
        Text(
            text = stringResource(R.string.button_countdown_link_to_event),
            modifier = Modifier.fillMaxWidth(),
            style = ChronosTypography.labelLarge,
            color = MaterialTheme.colorScheme.surfaceTint,
        )
    }
}


@Composable
private fun LinkedEvent(
    eventData: UiEventData,
    onUnlinkEvent: (eventId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .semantics { role = Role.Button }
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.surfaceContainerLow),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Row(
            modifier = Modifier
                .padding(horizontal = Spacing.lg)
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(Spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.Event,
                contentDescription = null,
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
            )
            Text(
                text = stringResource(R.string.label_countdown_linked_to_event),
                style = ChronosTypography.labelLarge,
                color = MaterialTheme.colorScheme.surfaceTint,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp, end = Spacing.lg),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    eventData.title,
                    style = ChronosTypography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    stringResource(
                        R.string.formatted_date_time,
                        eventData.timestamp.formattedLongDate(),
                        eventData.timestamp.formattedShortTime(),
                    ),
                    style = ChronosTypography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Row(
            Modifier.padding(start = 56.dp, end = Spacing.lg),
        ) {
            Row(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .semantics { role = Role.Button }
                    .clickable { onUnlinkEvent(eventData.eventId) },
            ) {
                Text(
                    stringResource(R.string.button_countdown_unlink_event),
                    style = ChronosTypography.labelLarge,
                    color = MaterialTheme.colorScheme.surfaceTint,
                )
            }
        }
    }
}

@Preview(name = "Without Linked Event")
@Composable
fun CalendarLinkButtonPreview() {
    EventLinkButton(onAddEvent = {}, onUnlinkEvent = {})
}

@Preview(name = "Linked Event")
@Composable
fun CalendarLinkButtonLinkedPreview() {
    EventLinkButton(
        onAddEvent = {},
        onUnlinkEvent = {},
        eventData = UiEventData("testId", "Judy's Birthday", Clock.System.now()),
    )
}