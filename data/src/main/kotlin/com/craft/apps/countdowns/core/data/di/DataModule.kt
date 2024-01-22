package com.craft.apps.countdowns.core.data.di

import com.craft.apps.countdowns.core.data.GenericCountdownRepository
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMyModelRepository(repository: CountdownRepository): GenericCountdownRepository
}
