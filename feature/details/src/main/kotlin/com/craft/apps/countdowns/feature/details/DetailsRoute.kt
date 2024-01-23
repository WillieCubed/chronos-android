package com.craft.apps.countdowns.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.craft.apps.countdowns.feature.details.ui.DetailScreen

@Composable
fun DetailsRoute(
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    viewModel.logView()
    DetailScreen(state, onGoBack, modifier)
}
