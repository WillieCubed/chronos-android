package com.craft.apps.countdowns.core.data.repository

import com.craft.apps.countdowns.core.data.local.LocalCountdownDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The central data source for [Countdown]s.
 */
class CountdownRepository @Inject constructor(
    private val localCountdownDataSource: LocalCountdownDataSource
) {

    val data: Flow<List<Countdown>> = localCountdownDataSource.getAllCountdowns()

    suspend fun addCountdown(countdown: Countdown) {
        localCountdownDataSource.addCountdown(countdown)
    }

    suspend fun deleteCountdownById(countdownId: Int) {
        localCountdownDataSource.deleteCountdown(countdownId)
    }

//    suspend fun editCountdown(id: Int, countdown: Countdown) {
//        TODO("Implement countdown editing")
//    }
}