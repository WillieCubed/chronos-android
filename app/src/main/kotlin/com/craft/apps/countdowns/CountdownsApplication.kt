package com.craft.apps.countdowns

import android.app.Application
import androidx.room.Room
import com.craft.apps.countdowns.core.data.AppDatabase
import com.craft.apps.countdowns.core.data.local.LocalCountdownDataSource
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import dagger.hilt.android.HiltAndroidApp

/**
 * A class that initializes app-wide resources.
 */
@HiltAndroidApp
class CountdownsApplication : Application()