package com.craft.apps.countdowns.feature.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.craft.apps.countdowns.core.model.formatted
import com.craft.apps.countdowns.ui.theme.ChronosTypography

@Composable
fun DetailScreen(
    uiState: DetailScreenUiState,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    fun handleEdit() {
        // TODO: Implement note editing functionality
    }

    when (uiState) {
        is DetailScreenUiState.Success -> {
            val countdown = uiState.countdown
            Column(modifier.fillMaxSize()) {
                Row {

                    Row {
                        // TODO: Date
                        Text(
                            text = countdown.expiration.formatted(),
                            style = ChronosTypography.displayLarge,
                            textAlign = TextAlign.Center
                        )
                        // TODO: Time
                        // TODO: Actual countdown
                    }
                }
                Column {
                    NotesCard(notes = countdown.notes, onEdit = ::handleEdit)
                    MetadataCard(
                        startedOn = countdown.creationTimestamp,
                        endsOn = countdown.expiration,
                    )
                }
            }
        }

        DetailScreenUiState.Loading -> {
            LoadingScreen(modifier)
        }

        is DetailScreenUiState.Error -> {
            // TODO: Change error based on exception
            Column(modifier) {
                Text("Whoops, couldn't load this countdown.", textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
internal fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    // TODO: Replace with a more elegant solution
    Column(modifier) {
        Text(
            "Currently loading....",
            textAlign = TextAlign.Center,
            style = ChronosTypography.titleLarge,
        )
    }
}

@Preview(showBackground = true, name = "Countdown Detail Screen")
@Composable
fun DetailScreenPreview() {
//    DetailScreen("")
}