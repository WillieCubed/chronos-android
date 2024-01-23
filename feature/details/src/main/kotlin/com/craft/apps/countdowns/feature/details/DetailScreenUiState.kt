package com.craft.apps.countdowns.feature.details

import com.craft.apps.countdowns.core.model.Countdown

sealed class DetailScreenUiState {

    data class Success(
        val countdown: Countdown,
    ) : DetailScreenUiState()

    data object Loading : DetailScreenUiState()
    data class Error(val exception: Throwable) : DetailScreenUiState()
}