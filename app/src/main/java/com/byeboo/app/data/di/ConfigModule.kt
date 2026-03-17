package com.byeboo.app.data.di

import com.byeboo.app.data.repositoryimpl.config.ConfigRepositoryImpl
import com.byeboo.app.domain.repository.config.ConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

    @Binds
    @Singleton
    abstract fun bindConfigRepository(
        configRepositoryImpl: ConfigRepositoryImpl
    ): ConfigRepository
}