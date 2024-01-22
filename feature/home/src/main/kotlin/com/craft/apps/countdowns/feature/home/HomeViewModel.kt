package com.craft.apps.countdowns.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craft.apps.countdowns.core.analytics.AnalyticsService
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.feature.home.ui.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A view model for a home screen.
 *
 * Data is loaded from a [CountdownRepository] immediately upon creation.
 *
 * Note that this logs countdown creations and deletions.
 */
@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val countdownRepository: CountdownRepository,
    // TODO: Fix modularization issue
    // private val countdownsAppSearchManager: CountdownsAppSearchManager,
    private val analyticsService: AnalyticsService,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        countdownRepository.data.map(HomeUiState::Success).stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading
        )

    /**
     * Creates a new countdown.
     */
    fun addCountdown(countdown: Countdown) {
        viewModelScope.launch {
            val createdId = countdownRepository.addCountdown(countdown)
            with(createdId) {
                analyticsService.logCreation(this.toString())
//                val result = countdownsAppSearchManager.addCountdown(
//                    countdown.copy(id = createdId).toSearchModel()
//                )
//                if (!result.isSuccess) {
//                    Log.e("HomeViewModel", "Could not add countdown to search index")
//                }
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
//                val result = countdownsAppSearchManager.removeCountdown(this)
//                if (!result.isSuccess) {
//                    Log.e("HomeViewModel", "Could not remove countdown from search index")
//                }
            }
        }
    }

    /**
     * Marks a countdown as featured.
     */
    fun featureCountdown(countdownId: Int) {
        viewModelScope.launch {
            countdownRepository.toggleFeatured(countdownId, true)
        }
    }
}

