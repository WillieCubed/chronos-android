package com.craft.apps.countdowns.core.data.local

import com.craft.apps.countdowns.core.data.repository.Countdown
import com.craft.apps.countdowns.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalCountdownDataSource @Inject constructor(
    private val dao: CountdownsDao,
    @IoDispatcher
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun addCountdown(countdown: Countdown) = withContext(coroutineDispatcher) {
        dao.insertCountdown(countdown.toLocalModel())
    }

    /**
     * Returns a [Flow] to subscribe to the current local [Countdown] data.
     */
    fun getAllCountdowns(): Flow<List<Countdown>> {
        return dao.queryAllCountdowns().map {
            it.map(LocalCountdownModel::toRepositoryModel)
        }
    }

    suspend fun deleteCountdown(countdownId: Int) = withContext(coroutineDispatcher) {
        val countdown = dao.getCountdownById(countdownId)
        dao.deleteCountdown(countdown)
    }
}