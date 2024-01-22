package com.craft.apps.countdowns.feature.home.ui

import com.craft.apps.countdowns.core.model.Countdown

sealed class HomeUiState {

    data class Success(
        val countdowns: List<Countdown> = listOf(),
    ) : HomeUiState()

    data object Loading : HomeUiState()
    data class Error(val exception: Throwable) : HomeUiState()
}