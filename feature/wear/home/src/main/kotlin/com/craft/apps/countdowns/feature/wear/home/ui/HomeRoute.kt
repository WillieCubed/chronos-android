package com.craft.apps.countdowns.feature.wear.home.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.core.model.formatted
import com.craft.apps.countdowns.ui.Loading

/**
 * The main home screen for the Chronos Wear OS app.
 */
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: WearHomeViewModel = hiltViewModel(),
) {
    // Note that the view model immediately starts loading data on init
    val state by viewModel.uiState.collectAsState()
    HomeScreen(state, modifier)
}

/**
 * A home screen with a default loading state.
 */
@Composable
internal fun HomeScreen(
    state: HomeUiState,
    modifier: Modifier = Modifier,
) {
    when (state) {
        HomeUiState.Loading -> Loading(modifier)
        is HomeUiState.Success -> Content(state.data, modifier)
    }
}

@Composable
internal fun Content(
    countdowns: List<Countdown>,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(modifier = modifier.fillMaxSize()) {
        items(countdowns) { countdown ->
            Chip(
                label = { Text(text = countdown.label) },
                secondaryLabel = { Text(text = countdown.expiration.formatted()) },
                onClick = { /* no-op */ },
                modifier = modifier.fillMaxWidth(),
            )
        }
    }
}
