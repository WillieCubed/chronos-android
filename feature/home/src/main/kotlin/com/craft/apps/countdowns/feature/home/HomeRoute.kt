package com.craft.apps.countdowns.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.craft.apps.countdowns.feature.home.ui.HomeScreen

@Composable
fun HomeRoute(
    onCountdownSelected: (countdownId: Int) -> Unit,
    onPinCountdown: (countdownId: Int) -> Unit,
    modifier: Modifier = Modifier,
    shouldCreateNewCountdown: Boolean = false,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    HomeScreen(
        state = state,
        onAddCountdown = viewModel::addCountdown,
        onDeleteCountdown = viewModel::removeCountdown,
        onCountdownSelected = onCountdownSelected,
        onPinCountdown = onPinCountdown,
        onFeatureCountdown = viewModel::featureCountdown,
        modifier = modifier,
        shouldCreateNewCountdown = shouldCreateNewCountdown,
    )
}

