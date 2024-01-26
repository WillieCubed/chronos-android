package com.craft.apps.countdowns.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing

@Composable
fun IconLabel(
    icon: ImageVector,
    contentDescription: String? = null,
    label: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
        Text(
            text = label,
            style = ChronosTypography.labelLarge,
        )
    }
}

@Preview
@Composable
fun IconLabelPreview() {
    IconLabel(
        icon = Icons.Default.Stars, contentDescription = null, label = "Favorites",
    )
}