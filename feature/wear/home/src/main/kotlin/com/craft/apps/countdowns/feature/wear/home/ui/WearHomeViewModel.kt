package com.craft.apps.countdowns.feature.wear.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craft.apps.countdowns.core.data.GenericCountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WearHomeViewModel @Inject constructor(
    countdownRepository: GenericCountdownRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        countdownRepository.data.map(HomeUiState::Success).stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                HomeUiState.Loading,
            )
}
