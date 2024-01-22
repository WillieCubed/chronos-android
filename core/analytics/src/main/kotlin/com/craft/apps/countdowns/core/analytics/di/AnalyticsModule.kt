package com.craft.apps.countdowns.core.analytics.di

import android.content.Context
import com.craft.apps.countdowns.core.analytics.AnalyticsService
import com.craft.apps.countdowns.core.analytics.CountdownsAnalyticsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object AnalyticsModule {
    @Provides
    fun provideAnalyticsService(@ApplicationContext context: Context): AnalyticsService =
        CountdownsAnalyticsService(context)
}
// TODO: Create separate analytics implementations for production and testing

//@Module
//@InstallIn(SingletonComponent::class)
//internal abstract class AnalyticsModule {
//    @Binds
//    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: FirebaseAnalyticsHelper): AnalyticsHelper
//
//    companion object {
//        @Provides
//        @Singleton
//        fun provideFirebaseAnalytics(): FirebaseAnalytics {
//            return Firebase.analytics
//        }
//    }
//}