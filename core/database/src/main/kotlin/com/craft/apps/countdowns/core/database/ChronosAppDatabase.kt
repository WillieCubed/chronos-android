package com.craft.apps.countdowns.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.craft.apps.countdowns.core.database.dao.CountdownsDao
import com.craft.apps.countdowns.core.database.dao.WidgetDataDao
import com.craft.apps.countdowns.core.database.entity.LocalCountdownModel
import com.craft.apps.countdowns.core.database.entity.LocalWidgetDataModel

/**
 * The primary offline database for the Countdowns app.
 *
 * This is a SQLite database powered by Android Room.
 */
@Database(
    entities = [
        LocalCountdownModel::class,
        LocalWidgetDataModel::class,
    ],
    version = 4,
    exportSchema = false,
)
internal abstract class ChronosAppDatabase : RoomDatabase() {

    /**
     * Returns a data access object for countdowns.
     */
    abstract fun countdownsDao(): CountdownsDao

    /**
     * Returns a data access object for widget countdown data.
     */
    abstract fun widgetDataDao(): WidgetDataDao

    companion object {

        private const val DB_NAME = "chronos"

        fun getInstance(context: Context): ChronosAppDatabase = Room.databaseBuilder(
            context,
            ChronosAppDatabase::class.java,
            DB_NAME,
        )
            // TODO: Remove in production
            .fallbackToDestructiveMigration()
            .build()
    }
}