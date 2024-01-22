package com.craft.apps.countdowns.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.craft.apps.countdowns.core.database.entity.LocalCountdownModel
import kotlinx.coroutines.flow.Flow

/**
 * A data access object for countdowns.
 */
@Dao
interface CountdownsDao {

    /**
     * Returns a flow of all countdowns in the database.
     */
    @Query("SELECT * from countdowns")
    fun queryAllCountdowns(): Flow<List<LocalCountdownModel>>

    /**
     * Returns a single countdown by ID.
     *
     * This should be used when you want to fetch
     */
    @Query("SELECT * from countdowns WHERE id = :id")
    fun getCountdownById(id: Int): LocalCountdownModel

    /**
     * Inserts a countdown into the database.
     *
     * Note that it's not necessary for the given countdown to have an ID; the ID that
     * is provided will be ignored.
     *
     * @return The ID of the inserted countdown
     */
    @Insert
    suspend fun insertCountdown(countdown: LocalCountdownModel): Long

    @Update
    suspend fun updateCountdown(countdown: LocalCountdownModel): Int

    @Delete
    suspend fun deleteCountdown(countdown: LocalCountdownModel)
}

