package com.byeboo.app.data.di

import com.byeboo.app.data.service.auth.UserService
import com.byeboo.app.data.service.offboarding.OffboardingJourneyService
import com.byeboo.app.data.service.offboarding.OffboardingNewJourneyService
import com.byeboo.app.data.service.quest.QuestBehaviorService
import com.byeboo.app.data.service.quest.QuestDetailService
import com.byeboo.app.data.service.quest.QuestRecordedDetailService
import com.byeboo.app.data.service.quest.QuestRecordingService
import com.byeboo.app.data.service.quest.QuestService
import com.byeboo.app.data.service.quest.QuestTipService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService = retrofit.create(
        UserService::class.java
    )

    @Provides
    @Singleton
    fun providesQuestDetailService(retrofit: Retrofit): QuestDetailService = retrofit.create(
        QuestDetailService::class.java
    )

    @Provides
    @Singleton
    fun providesQuestService(retrofit: Retrofit): QuestService = retrofit.create(
        QuestService::class.java
    )

    @Provides
    @Singleton
    fun providesQuestBehaviorService(retrofit: Retrofit): QuestBehaviorService = retrofit.create(
        QuestBehaviorService::class.java
    )

    @Provides
    @Singleton
    fun providesQuestTipService(retrofit: Retrofit): QuestTipService = retrofit.create(
        QuestTipService::class.java
    )

    @Provides
    @Singleton
    fun providesQuestRecordingService(retrofit: Retrofit): QuestRecordingService = retrofit.create(
        QuestRecordingService::class.java
    )

    @Provides
    @Singleton
    fun providesQuestRecordedDetailService(retrofit: Retrofit): QuestRecordedDetailService =
        retrofit.create(
            QuestRecordedDetailService::class.java
        )

    @Provides
    @Singleton
    fun providesOffboardingJourneyService(retrofit: Retrofit): OffboardingJourneyService =
        retrofit.create(
            OffboardingJourneyService::class.java
        )

    @Provides
    @Singleton
    fun providesOffboardingNewJourneyService(retrofit: Retrofit): OffboardingNewJourneyService =
        retrofit.create(
            OffboardingNewJourneyService::class.java
        )
}
