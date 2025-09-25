package com.byeboo.app.data.di

import com.byeboo.app.core.util.MixpanelUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MixpanelModule {

    @Provides
    @Singleton
    fun provideMixpanelUtil(): MixpanelUtil {
        return MixpanelUtil()
    }
}
