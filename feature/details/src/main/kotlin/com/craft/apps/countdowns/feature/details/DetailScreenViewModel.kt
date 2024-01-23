package com.craft.apps.countdowns.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craft.apps.countdowns.core.analytics.AnalyticsService
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * A view model for interacting with a countdown's detailed information display.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val analyticsService: AnalyticsService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val uiState: StateFlow<DetailScreenUiState> =
        savedStateHandle.getStateFlow<Int?>("id", null).filterNotNull()
            .flatMapLatest { id -> countdownRepository[id] }
            .catch { DetailScreenUiState.Error(it) }
            .map { model -> DetailScreenUiState.Success(model) }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                DetailScreenUiState.Loading,
            )

    fun logView() {
        analyticsService.logScreenVisit(AnalyticsService.ScreenId.COUNTDOWN_DETAILS)
    }
}

