package com.craft.apps.countdowns.core.data

import com.craft.apps.countdowns.core.model.Countdown
import kotlinx.coroutines.flow.Flow

/**
 * A repository to store and modify [Countdown] data.
 */
interface GenericCountdownRepository {
    val data: Flow<List<Countdown>>
    suspend fun addCountdown(countdown: Countdown): Int
    suspend fun deleteCountdownById(countdownId: Int)
    suspend fun editCountdown(countdown: Countdown)
    operator fun get(countdownId: Int): Flow<Countdown>
    suspend fun toggleFeatured(countdownId: Int, isFeatured: Boolean = true)
//    fun observeModelById(id: Long): Flow<MyModel>
}
