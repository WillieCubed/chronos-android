package com.craft.apps.countdowns.feature.wear.home.ui

import com.craft.apps.countdowns.core.model.Countdown

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val data: List<Countdown>) : HomeUiState
}
