package com.craft.apps.countdowns.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.craft.apps.countdowns.core.data.local.CountdownsDao
import com.craft.apps.countdowns.core.data.local.LocalCountdownModel

/**
 * The primary offline database for the Countdowns app.
 */
@Database(
    entities = [LocalCountdownModel::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun countdownsDao(): CountdownsDao

    companion object {

        private const val DB_NAME = "countdowns"

        fun getInstance(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME,
            ).build()
    }
}