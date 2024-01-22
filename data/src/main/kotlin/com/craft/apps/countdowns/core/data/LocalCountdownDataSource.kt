package com.craft.apps.countdowns.core.data

import com.craft.apps.countdowns.core.coroutines.IoDispatcher
import com.craft.apps.countdowns.core.database.dao.CountdownsDao
import com.craft.apps.countdowns.core.database.entity.LocalCountdownModel
import com.craft.apps.countdowns.core.database.entity.toDataModel
import com.craft.apps.countdowns.core.database.entity.toDatabaseModel
import com.craft.apps.countdowns.core.model.Countdown
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A data source that stores and modifies countdowns on this device.
 */
class LocalCountdownDataSource @Inject constructor(
    private val dao: CountdownsDao,
    /**
     * An IO dispatcher used to handle data reads/writes.
     */
    @IoDispatcher
    private val coroutineDispatcher: CoroutineDispatcher,
) {
    /**
     * Returns a [Flow] to subscribe to the current local [Countdown] data.
     */
    fun getAllCountdowns(): Flow<List<Countdown>> {
        return dao.queryAllCountdowns().map {
            it.map(LocalCountdownModel::toDataModel)
        }
    }

    /**
     * Adds a [Countdown] to local storage.
     */
    suspend fun addCountdown(countdown: Countdown) = withContext(coroutineDispatcher) {
        return@withContext dao.insertCountdown(countdown.toDatabaseModel()).toInt()
    }

    /**
     * Deletes a [Countdown] from local storage.
     */
    suspend fun deleteCountdown(countdownId: Int) = withContext(coroutineDispatcher) {
        val countdown = dao.getCountdownById(countdownId)
        dao.deleteCountdown(countdown)
    }

    /**
     * Updates a [Countdown] in local storage.
     *
     * This operation replaces the countdown with the given ID with the given countdown.
     */
    suspend fun updateCountdown(countdown: Countdown) = withContext(coroutineDispatcher) {
        return@withContext dao.updateCountdown(countdown.toDatabaseModel())
    }
}