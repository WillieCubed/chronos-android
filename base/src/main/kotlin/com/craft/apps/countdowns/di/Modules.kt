package com.craft.apps.countdowns.di

import android.content.Context
import com.craft.apps.countdowns.analytics.AnalyticsService
import com.craft.apps.countdowns.analytics.CountdownsAnalyticsService
import com.craft.apps.countdowns.core.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideCountdownsDao(appDatabase: AppDatabase) = appDatabase.countdownsDao()
}

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {

    @Provides
    fun provideCoroutineScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MainImmediateDispatcher
    @Provides
    fun provideMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}

@InstallIn(SingletonComponent::class)
@Module
object AnalyticsModule {
    @Provides
    fun provideAnalyticsService(@ApplicationContext context: Context): AnalyticsService =
        CountdownsAnalyticsService(context)
}


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
