package com.craft.apps.countdowns.core.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craft.apps.countdowns.analytics.AnalyticsService
import com.craft.apps.countdowns.core.data.repository.Countdown
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val analyticsService: AnalyticsService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailScreenUiState>(DetailScreenUiState.Loading)

    val uiState: StateFlow<DetailScreenUiState> = _uiState

    fun loadById(countdownId: Int) {
        viewModelScope.launch {
            countdownRepository[countdownId].catch {
                _uiState.value = DetailScreenUiState.Error(it)
            }.collect {
                _uiState.value = DetailScreenUiState.Success(it)
            }
        }
    }
}

sealed class DetailScreenUiState {

    data class Success(
        val countdown: Countdown,
    ) : DetailScreenUiState()

    data object Loading : DetailScreenUiState()
    data class Error(val exception: Throwable) : DetailScreenUiState()
}
