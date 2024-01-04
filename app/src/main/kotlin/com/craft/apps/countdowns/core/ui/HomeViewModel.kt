package com.craft.apps.countdowns.core.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craft.apps.countdowns.search.CountdownsAppSearchManager
import com.craft.apps.countdowns.core.data.repository.Countdown
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import com.craft.apps.countdowns.search.toSearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val countdownRepository: CountdownRepository,
    private val countdownsAppSearchManager: CountdownsAppSearchManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LatestHomeUiState>(LatestHomeUiState.Loading)

    val uiState: StateFlow<LatestHomeUiState> = _uiState

    init {
        viewModelScope.launch {
            countdownRepository.data.collect {
                _uiState.value = LatestHomeUiState.Success(it)
            }
        }
    }

    fun addCountdown(countdown: Countdown) {
        // Creating countdown
        Log.d("HomeViewModel", countdown.toString())
        viewModelScope.launch {
            countdownRepository.addCountdown(countdown)
            val result = countdownsAppSearchManager.addCountdown(countdown.toSearchModel())
            if (!result.isSuccess) {
                Log.e("HomeViewModel", "Could not add countdown to search index")
            }
        }
    }

    fun removeCountdown(countdown: Countdown) {
        viewModelScope.launch {
            countdownRepository.deleteCountdownById(countdown.id)
            val result = countdownsAppSearchManager.removeCountdown(countdown.id)
            if (!result.isSuccess) {
                Log.e("HomeViewModel", "Could not remove countdown from search index")
            }
        }

    }
}

sealed class LatestHomeUiState {
    data class Success(
        val countdowns: List<Countdown> = listOf(),
    ) : LatestHomeUiState()

    data object Loading : LatestHomeUiState()
    data class Error(val exception: Throwable) : LatestHomeUiState()
}


val testCountdowns = listOf(
    Countdown(1, "Test", Instant.fromEpochMilliseconds(System.currentTimeMillis())),
    Countdown(2, "Test 2", Instant.fromEpochMilliseconds(System.currentTimeMillis())),
    Countdown(3, "Test 3", Instant.fromEpochMilliseconds(System.currentTimeMillis())),
)