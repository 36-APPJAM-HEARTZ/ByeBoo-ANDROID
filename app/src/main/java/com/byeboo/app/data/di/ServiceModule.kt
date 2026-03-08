package com.byeboo.app.data.di

import com.byeboo.app.core.network.qualifier.Auth
import com.byeboo.app.data.service.NewJourneyService
import com.byeboo.app.data.service.auth.AuthService
import com.byeboo.app.data.service.auth.UserService
import com.byeboo.app.data.service.mypage.BlockedUsersService
import com.byeboo.app.data.service.notification.NotificationService
import com.byeboo.app.data.service.offboarding.OffboardingJourneyService
import com.byeboo.app.data.service.offboarding.OffboardingQuestCompletedService
import com.byeboo.app.data.service.quest.QuestAiAnswerService
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
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providesAuthService(
        @Auth retrofit: Retrofit,
    ): AuthService =
        retrofit.create(
            AuthService::class.java,
        )

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService =
        retrofit.create(
            UserService::class.java,
        )

    @Provides
    @Singleton
    fun providesQuestDetailService(retrofit: Retrofit): QuestDetailService =
        retrofit.create(
            QuestDetailService::class.java,
        )

    @Provides
    @Singleton
    fun providesQuestService(retrofit: Retrofit): QuestService =
        retrofit.create(
            QuestService::class.java,
        )

    @Provides
    @Singleton
    fun providesQuestTipService(retrofit: Retrofit): QuestTipService =
        retrofit.create(
            QuestTipService::class.java,
        )

    @Provides
    @Singleton
    fun providesQuestBehaviorService(retrofit: Retrofit): QuestBehaviorService =
        retrofit.create(
            QuestBehaviorService::class.java,
        )

    @Provides
    @Singleton
    fun providesQuestRecordingService(retrofit: Retrofit): QuestRecordingService =
        retrofit.create(
            QuestRecordingService::class.java,
        )

    @Provides
    @Singleton
    fun providesQuestRecordedDetailService(retrofit: Retrofit): QuestRecordedDetailService =
        retrofit.create(
            QuestRecordedDetailService::class.java,
        )

    @Provides
    @Singleton
    fun providesOffboardingJourneyService(retrofit: Retrofit): OffboardingJourneyService =
        retrofit.create(
            OffboardingJourneyService::class.java,
        )

    @Provides
    @Singleton
    fun providesOffboardingNewJourneyService(retrofit: Retrofit): NewJourneyService =
        retrofit.create(
            NewJourneyService::class.java,
        )

    @Provides
    @Singleton
    fun providesOffboardingQuestCompletedService(retrofit: Retrofit): OffboardingQuestCompletedService =
        retrofit.create(
            OffboardingQuestCompletedService::class.java,
        )

    @Provides
    @Singleton
    fun providesNotificationService(retrofit: Retrofit): NotificationService =
        retrofit.create(
            NotificationService::class.java,
        )

    @Provides
    @Singleton
    fun providesQuestAiAnswerService(retrofit: Retrofit): QuestAiAnswerService =
        retrofit.create(
            QuestAiAnswerService::class.java,
        )

    @Provides
    @Singleton
    fun providesBlockedUsersService(retrofit: Retrofit): BlockedUsersService =
        retrofit.create(
            BlockedUsersService::class.java,
        )
}
