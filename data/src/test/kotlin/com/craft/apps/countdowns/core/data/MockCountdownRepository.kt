package com.craft.apps.countdowns.core.data

import com.craft.apps.countdowns.core.data.GenericCountdownRepository
import com.craft.apps.countdowns.core.model.Countdown
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MockCountdownRepository : GenericCountdownRepository {

    private val _countdowns: MutableList<Countdown> = testCountdowns

    override val data: Flow<List<Countdown>>
        get() = flow { emit(_countdowns) }

    override suspend fun addCountdown(countdown: Countdown): Int {
        _countdowns.add(countdown)
        return _countdowns.size
    }

    override suspend fun deleteCountdownById(countdownId: Int) {
        _countdowns.removeIf { it.id == countdownId }
    }

    override suspend fun editCountdown(countdown: Countdown) {
        val index = _countdowns.indexOfFirst { it.id == countdown.id }
        if (index == -1) {
            throw Exception("Countdown with ID not found")
        }
        _countdowns[index] = countdown
    }

    override fun get(countdownId: Int): Flow<Countdown> = flow {
        val countdown = _countdowns.find { it.id == countdownId }
            ?: throw Exception("Countdown with ID not found")
        emit(countdown)
    }

    override suspend fun toggleFeatured(countdownId: Int, isFeatured: Boolean) {
        val index = _countdowns.indexOfFirst { it.id == countdownId }
        if (index == -1) {
            throw Exception("Countdown with ID not found")
        }
        val countdown = _countdowns[index]
        _countdowns[index] = countdown.copy(featured = isFeatured)
    }

    companion object {
        val testCountdowns = mutableListOf(
            Countdown(
                1,
                "Countdown 1",
                Instant.parse("2024-01-21T00:00"),
                Clock.System.now(),
                "These are some notes.",
                false,
            ),
            Countdown(
                2,
                "Countdown 2",
                Instant.parse("2024-01-22T00:00"),
                Clock.System.now(),
                "These are some notes.",
                true,
            ),
            Countdown(
                3,
                "Countdown 3",
                Instant.parse("2024-01-23T00:00"),
                Clock.System.now(),
                "These are some notes.",
                false,
            ),
        )
    }
}