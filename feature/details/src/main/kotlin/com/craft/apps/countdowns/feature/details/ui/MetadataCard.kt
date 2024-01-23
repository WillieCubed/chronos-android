package com.craft.apps.countdowns.feature.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.craft.apps.countdowns.core.model.formatted
import com.craft.apps.countdowns.feature.details.R
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing
import com.craft.apps.countdowns.ui.util.testCountdowns
import kotlinx.datetime.Instant

@Composable
internal fun MetadataCard(startedOn: Instant, expiresOn: Instant) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = stringResource(R.string.label_countdown_details_created_on),
                        style = ChronosTypography.titleMedium
                    )
                    Text(
                        text = startedOn.formatted(),
                        style = ChronosTypography.labelMedium,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        // TODO: Change label based on countdown expiry to "ended on"
                        text = stringResource(R.string.label_countdown_details_ends_on),
                        style = ChronosTypography.titleMedium
                    )
                    Text(
                        text = expiresOn.formatted(),
                        style = ChronosTypography.labelMedium,
                    )
                }
            }
        }
    }
}

@Composable
internal fun NotesCard(notes: String, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.lg, end = Spacing.sm, top = Spacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Notes",
                    style = ChronosTypography.titleMedium
                )
                IconButton(
                    enabled = false, // TODO: Remove once edit functionality is supported
                    onClick = onEdit,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.icon_content_description_edit_note),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.lg)
            ) {
                val notesContent = notes.ifEmpty { stringResource(R.string.state_empty_notes_card) }
                val style = if (notes.isEmpty()) {
                    ChronosTypography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    ChronosTypography.bodyMedium
                }
                Text(text = notesContent, style = style)
            }
        }
    }
}

@Preview
@Composable
private fun MetadataCardPreview() {
    MetadataCard(
        startedOn = testCountdowns[0].creationTimestamp,
        expiresOn = testCountdowns[0].expiration,
    )
}

@Preview(name = "NotesCard")
@Composable
private fun NotesCardPreview() {
    NotesCard(
        notes = testCountdowns[0].notes,
        onEdit = {},
    )
}

@Preview(name = "NotesCard - Empty")
@Composable
private fun NotesCardEmptyPreview() {
    NotesCard(
        notes = "",
        onEdit = {},
    )
}