package com.craft.apps.countdowns.core.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craft.apps.countdowns.analytics.AnalyticsService
import com.craft.apps.countdowns.core.data.repository.Countdown
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import com.craft.apps.countdowns.search.CountdownsAppSearchManager
import com.craft.apps.countdowns.search.toSearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject

/**
 * A view model for the [HomeScreen].
 *
 * Data is loaded from a [CountdownRepository] immediately upon creation.
 *
 * Note that this logs countdown creations and deletions.
 */
@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val countdownRepository: CountdownRepository,
    private val countdownsAppSearchManager: CountdownsAppSearchManager,
    private val analyticsService: AnalyticsService,
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

    /**
     * Creates a new countdown.
     */
    fun addCountdown(countdown: Countdown) {
        viewModelScope.launch {
            val createdId = countdownRepository.addCountdown(countdown)
            with(createdId) {
                analyticsService.logCreation(this.toString())
                val result = countdownsAppSearchManager.addCountdown(
                    countdown.copy(id = createdId).toSearchModel()
                )
                if (!result.isSuccess) {
                    Log.e("HomeViewModel", "Could not add countdown to search index")
                }
            }
        }
    }

    /**
     * Removes a countdown from the view.
     *
     * @param countdownId The ID of the countdown to remove
     */
    fun removeCountdown(countdownId: Int) {
        viewModelScope.launch {
            with(countdownId) {
                countdownRepository.deleteCountdownById(this)
                analyticsService.logDeletion(this.toString())
                val result = countdownsAppSearchManager.removeCountdown(this)
                if (!result.isSuccess) {
                    Log.e("HomeViewModel", "Could not remove countdown from search index")
                }
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