package com.craft.apps.countdowns.core.database.di

import android.content.Context
import com.craft.apps.countdowns.core.database.ChronosAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        ChronosAppDatabase.getInstance(context)
}

@InstallIn(SingletonComponent::class)
@Module
internal object DaosModule {

    @Provides
    fun provideCountdownsDao(appDatabase: ChronosAppDatabase) = appDatabase.countdownsDao()

    @Provides
    fun provideWidgetDataDao(appDatabase: ChronosAppDatabase) = appDatabase.widgetDataDao()
}