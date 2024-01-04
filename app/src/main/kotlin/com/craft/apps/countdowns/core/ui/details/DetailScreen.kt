package com.craft.apps.countdowns.core.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.craft.apps.countdowns.core.data.repository.formatted
import com.craft.apps.countdowns.core.theme.AppTypographyStyles
import com.craft.apps.countdowns.core.ui.home.testCountdowns

@Composable
fun DetailScreen(
    countdownId: Int,
    viewModel: DetailScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    viewModel.loadById(countdownId)

    when (uiState) {
        is DetailScreenUiState.Success -> {
            val countdown = (uiState as DetailScreenUiState.Success).countdown
            Column(Modifier.fillMaxSize()) {
                Row {
                    Text(
                        text = countdown.label,
                        style = AppTypographyStyles.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Row {
                    // TODO: Date
                    Text(
                        text = countdown.timestamp.formatted(),
                        style = AppTypographyStyles.displayLarge,
                        textAlign = TextAlign.Center
                    )
                    // TODO: Time
                    // TODO: Actual countdown
                }
            }
        }

        DetailScreenUiState.Loading -> {
            Text("Currently loading....", textAlign = TextAlign.Center)
        }

        is DetailScreenUiState.Error -> {
            // TODO: Change error based on exception
            Text("Whoops, couldn't load this countdown.", textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true, name = "Countdown Detail Screen")
@Composable
fun DetailScreenPreview() {
    DetailScreen(testCountdowns[0].id)
}