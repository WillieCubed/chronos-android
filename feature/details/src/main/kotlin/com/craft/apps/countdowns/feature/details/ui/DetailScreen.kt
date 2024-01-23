@file:OptIn(ExperimentalMaterial3Api::class)

package com.craft.apps.countdowns.feature.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.feature.details.DetailScreenUiState
import com.craft.apps.countdowns.feature.details.R
import com.craft.apps.countdowns.feature.details.ui.timer.LargeCountdownTimer
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing
import com.craft.apps.countdowns.ui.util.CountdownDisplayStyle
import com.craft.apps.countdowns.ui.util.testCountdowns

@Composable
fun DetailScreen(
    uiState: DetailScreenUiState,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    fun handleEdit() {
        // TODO: Implement note editing functionality
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            val title =
                if (uiState is DetailScreenUiState.Success) uiState.countdown.label else "Countdown details"
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                title = { Text(title) },
                navigationIcon = {
                    IconButton(modifier = Modifier.testTag("nav_back"), onClick = { onGoBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // TODO: Add delete
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->
        when (uiState) {
            is DetailScreenUiState.Success -> {
                DetailsContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    uiState = uiState,
                    onEdit = ::handleEdit,
                )
            }

            DetailScreenUiState.Loading -> {
                LoadingScreen(modifier.padding(contentPadding))
            }

            is DetailScreenUiState.Error -> {
                // TODO: Change error based on exception
                Column(
                    modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.error_loading_countdown_details),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
internal fun DetailsContent(
    modifier: Modifier,
    uiState: DetailScreenUiState.Success,
    onEdit: () -> Unit,
) {
    val countdown = uiState.countdown
    Column(modifier) {
        Row() {
            // TODO: Make this fill 
            LargeCountdownTimer(
                modifier = Modifier.fillMaxWidth(),
                expirationTime = countdown.expiration,
                displayStyle = CountdownDisplayStyle.DYNAMIC,
            )
        }
        Column(
            Modifier
                .padding(Spacing.lg)
                .offset(y = (-48).dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            NotesCard(notes = countdown.notes, onEdit = onEdit)
            MetadataCard(
                startedOn = countdown.creationTimestamp,
                expiresOn = countdown.expiration,
            )
        }
    }
}

@Composable
internal fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    // TODO: Replace with a more elegant solution
    Column(modifier.fillMaxSize()) {
        Text(
            stringResource(R.string.loading_countdown_details),
            textAlign = TextAlign.Center,
            style = ChronosTypography.titleLarge,
        )
    }
}

@Preview(showBackground = true, name = "Countdown Detail Screen")
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        DetailScreenUiState.Success(
            testCountdowns[0]
        ),
        onGoBack = {},
    )
}

@Preview(showBackground = true, name = "Countdown Detail Screen - Loading")
@Composable
fun DetailScreenLoadingPreview() {
    DetailScreen(
        DetailScreenUiState.Loading,
        onGoBack = {},
    )
}

@Preview(showBackground = true, name = "Countdown Detail Screen - Error")
@Composable
fun DetailScreenErrorPreview() {
    DetailScreen(
        DetailScreenUiState.Error(Exception()),
        onGoBack = {},
    )
}