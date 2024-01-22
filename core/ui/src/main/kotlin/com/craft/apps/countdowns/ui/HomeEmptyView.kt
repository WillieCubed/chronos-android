package com.craft.apps.countdowns.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.ui.theme.ChronosTypography

@Composable
fun HomeEmptyView(
    onCreateCountdown: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "Looks like you don't have any countdowns yet.",
            style = ChronosTypography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onCreateCountdown) {
            Text("Create a countdown")
        }
    }
}