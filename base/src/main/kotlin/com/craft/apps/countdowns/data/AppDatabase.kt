package com.craft.apps.countdowns.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.craft.apps.countdowns.data.local.CountdownsDao
import com.craft.apps.countdowns.data.local.LocalCountdownModel
import com.craft.apps.countdowns.data.local.widget.LocalWidgetDataModel
import com.craft.apps.countdowns.data.local.widget.WidgetDataDao

/**
 * The primary offline database for the Countdowns app.
 */
@Database(
    entities = [LocalCountdownModel::class, LocalWidgetDataModel::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Returns a data access object for countdowns.
     */
    abstract fun countdownsDao(): CountdownsDao

    /**
     * Returns a data access object for widget countdown data.
     */
    abstract fun widgetDataDao(): WidgetDataDao

    companion object {

        private const val DB_NAME = "countdowns"

        fun getInstance(context: Context): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME,
        ).fallbackToDestructiveMigration().build()
    }
}