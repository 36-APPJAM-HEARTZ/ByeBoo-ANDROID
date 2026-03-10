package com.byeboo.app.data.di

import android.content.Context
import com.byeboo.app.data.validatorimpl.BadWordValidatorImpl
import com.byeboo.app.domain.model.auth.BadWordValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ValidatorModule {
    @Provides
    @Singleton
    fun provideBadWordValidator(
        @ApplicationContext context: Context,
    ): BadWordValidator = BadWordValidatorImpl(context)
}
