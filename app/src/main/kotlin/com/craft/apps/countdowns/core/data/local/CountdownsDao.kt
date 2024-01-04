package com.craft.apps.countdowns.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * A data access object for countdowns.
 */
@Dao
interface CountdownsDao {

    @Query("SELECT * from countdowns")
    fun queryAllCountdowns(): Flow<List<LocalCountdownModel>>

    @Query("SELECT * from countdowns WHERE id = :id")
    fun getCountdownById(id: Int): LocalCountdownModel

    @Insert
    suspend fun insertCountdown(countdown: LocalCountdownModel)

    @Delete
    suspend fun deleteCountdown(countdown: LocalCountdownModel)
}

