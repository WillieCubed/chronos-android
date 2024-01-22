package com.craft.apps.countdowns.core.data.repository

import com.craft.apps.countdowns.core.data.GenericCountdownRepository
import com.craft.apps.countdowns.core.data.LocalCountdownDataSource
import com.craft.apps.countdowns.core.model.Countdown
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

/**
 * The central data source for [Countdown]s.
 */
class CountdownRepository @Inject constructor(
    /**
     * An on-device data store used as the primary source of truth for the app.
     */
    private val localCountdownDataSource: LocalCountdownDataSource,
) : GenericCountdownRepository {

    /**
     * The main data stream for all countdowns stored in this repository.
     */
    override val data: Flow<List<Countdown>> = localCountdownDataSource.getAllCountdowns()

    // Map then filter since the flow returns a sequential stream of Lists, which then need
    // to be filtered.
    private fun getSingleCountdown(countdownId: Int): Flow<Countdown> =
        data.map { it.filter { countdown -> countdown.id == countdownId } }.map { it[0] }

    /**
     * Returns a single [Countdown] flow by its ID.
     */
    override operator fun get(countdownId: Int) = getSingleCountdown(countdownId)

    /**
     * Adds a countdown to this repository.
     *
     * This currently only adds a countdown to the device's local storage.
     *
     * @return The ID of the new countdown
     */
    override suspend fun addCountdown(countdown: Countdown): Int {
        return localCountdownDataSource.addCountdown(countdown)
    }

    /**
     * Deletes a countdown from this repository.
     *
     * This currently only removes a countdown to the device's local storage. Removing a countdown
     * deletes it from memory.
     *
     * @return The ID of the countdown to remove
     */
    override suspend fun deleteCountdownById(countdownId: Int) {
        localCountdownDataSource.deleteCountdown(countdownId)
    }

    /**
     * Updates a countdown with new data.
     *
     * This requires the countdown to have an ID. All fields from the given countdown will replace
     * the one with its ID.
     *
     * @param countdown The countdown to update
     */
    override suspend fun editCountdown(countdown: Countdown) {
        localCountdownDataSource.updateCountdown(countdown)
    }

    /**
     * Marks a countdown as featured.
     */
    override suspend fun toggleFeatured(countdownId: Int, isFeatured: Boolean) {
        // TODO: Find a more elegant solution to this.
        val updatedCountdown = getSingleCountdown(countdownId).single().copy(featured = isFeatured)
        localCountdownDataSource.updateCountdown(updatedCountdown)
    }
}