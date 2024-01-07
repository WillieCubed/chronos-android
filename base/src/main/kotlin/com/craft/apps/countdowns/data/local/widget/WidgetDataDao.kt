package com.craft.apps.countdowns.data.local.widget

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * A data access object for countdowns.
 */
@Dao
interface WidgetDataDao {

    /**
     * Returns a flow of all widget data in the database.
     */
    @Transaction
    @Query("SELECT * from widget_data")
    fun queryWidgetData(): Flow<List<WidgetDataAndCountdownModel>>

    /**
     * Inserts a countdown into the database.
     *
     * @return The ID of the inserted countdown
     */
    @Insert
    suspend fun insertWidgetData(countdown: LocalWidgetDataModel): Long

    @Delete
    suspend fun deleteWidgetData(countdown: LocalWidgetDataModel)
}

