package com.craft.apps.countdowns.feature.widgets.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import com.craft.apps.countdowns.feature.widgets.data.WidgetCountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigureWidgetViewModel @Inject constructor(
    private val widgetCountdownRepository: WidgetCountdownRepository,
    private val countdownRepository: CountdownRepository,
) : ViewModel() {

    fun selectCountdown(countdownId: Int, widgetId: Int) {
        viewModelScope.launch {
            widgetCountdownRepository.addCountdownWidget(countdownId, widgetId.toString())
        }
    }
}