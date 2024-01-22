package com.craft.apps.countdowns.feature.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.core.model.formatted
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing
import kotlinx.datetime.Instant

@Composable
internal fun MetadataCard(startedOn: Instant, endsOn: Instant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.Start),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Started on",
                        style = ChronosTypography.labelLarge
                    )
                    Text(
                        text = startedOn.formatted(),
                        style = ChronosTypography.titleSmall,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        // TODO: Change label based on countdown expiry to "ended on"
                        text = "Ends on",
                        style = ChronosTypography.labelLarge
                    )
                    Text(
                        text = endsOn.formatted(),
                        style = ChronosTypography.titleSmall,
                    )
                }
            }
        }
    }
}

@Composable
internal fun NotesCard(notes: String, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Notes",
                    style = ChronosTypography.titleMedium
                )
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit note")
                }
            }

        }
    }
}