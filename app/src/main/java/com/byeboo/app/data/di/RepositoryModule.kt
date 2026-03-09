package com.byeboo.app.data.di

import com.byeboo.app.data.repositoryimpl.NewJourneyRepositoryImpl
import com.byeboo.app.data.repositoryimpl.auth.AuthRepositoryImpl
import com.byeboo.app.data.repositoryimpl.auth.TokenRepositoryImpl
import com.byeboo.app.data.repositoryimpl.auth.UserRepositoryImpl
import com.byeboo.app.data.repositoryimpl.fcm.FcmTokenRepositoryImpl
import com.byeboo.app.data.repositoryimpl.mypage.BlockedUsersRepositoryImpl
import com.byeboo.app.data.repositoryimpl.offboarding.OffboardingJourneyRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.OffboardingQuestCompletedRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.QuestAiAnswerRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.QuestCommonRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.QuestInProgressRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.QuestRecordedDetailRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.QuestStateRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.QuestTipRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.behavior.QuestBehaviorRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.behavior.QuestDetailBehaviorRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.recording.QuestDetailRecordingRepositoryImpl
import com.byeboo.app.data.repositoryimpl.quest.recording.QuestRecordingRepositoryImpl
import com.byeboo.app.domain.repository.NewJourneyRepository
import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import com.byeboo.app.domain.repository.mypage.BlockedUsersRepository
import com.byeboo.app.domain.repository.offboarding.OffboardingJourneyRepository
import com.byeboo.app.domain.repository.offboarding.OffboardingQuestCompletedRepository
import com.byeboo.app.domain.repository.quest.QuestAiAnswerRepository
import com.byeboo.app.domain.repository.quest.QuestBehaviorRepository
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.domain.repository.quest.QuestDetailBehaviorRepository
import com.byeboo.app.domain.repository.quest.QuestDetailRecordingRepository
import com.byeboo.app.domain.repository.quest.QuestInProgressRepository
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import com.byeboo.app.domain.repository.quest.QuestRecordingRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import com.byeboo.app.domain.repository.quest.QuestTipRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindQuestDetailBehaviorRepository(
        questDetailBehaviorRepositoryImpl: QuestDetailBehaviorRepositoryImpl,
    ): QuestDetailBehaviorRepository

    @Binds
    @Singleton
    abstract fun bindQuestDetailRecordingRepository(
        questDetailRecordingRepositoryImpl: QuestDetailRecordingRepositoryImpl,
    ): QuestDetailRecordingRepository

    @Binds
    @Singleton
    abstract fun bindsTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository

    @Binds
    @Singleton
    abstract fun bindsQuestStateRepository(questStateRepositoryImpl: QuestStateRepositoryImpl): QuestStateRepository

    @Binds
    @Singleton
    abstract fun bindsQuestInProgressRepository(questInProgressRepositoryImpl: QuestInProgressRepositoryImpl): QuestInProgressRepository

    @Binds
    @Singleton
    abstract fun bindsQuestCompletedRepository(
        offboardingQuestCompletedRepositoryImpl: OffboardingQuestCompletedRepositoryImpl,
    ): OffboardingQuestCompletedRepository

    @Binds
    @Singleton
    abstract fun bindQuestBehaviorAnswerRepository(questBehaviorAnswerRepositoryImpl: QuestBehaviorRepositoryImpl): QuestBehaviorRepository

    @Binds
    @Singleton
    abstract fun bindQuestTipRepository(questTipRepositoryImpl: QuestTipRepositoryImpl): QuestTipRepository

    @Binds
    @Singleton
    abstract fun bindQuestRecordingRepository(questRecordingRepositoryImpl: QuestRecordingRepositoryImpl): QuestRecordingRepository

    @Binds
    @Singleton
    abstract fun bindQuestRecordedDetailRepository(
        questRecordedDetailRepositoryImpl: QuestRecordedDetailRepositoryImpl,
    ): QuestRecordedDetailRepository

    @Binds
    @Singleton
    abstract fun bindOffboardingJourneyRepository(
        offboardingJourneyRepositoryImpl: OffboardingJourneyRepositoryImpl,
    ): OffboardingJourneyRepository

    @Binds
    @Singleton
    abstract fun bindNewJourneyRepository(newJourneyRepositoryImpl: NewJourneyRepositoryImpl): NewJourneyRepository

    @Binds
    @Singleton
    abstract fun bindFcmTokenRepository(fcmTokenRepositoryImpl: FcmTokenRepositoryImpl): FcmTokenRepository

    @Binds
    @Singleton
    abstract fun bindQuestAiAnswerRepository(questAiAnswerRepositoryImpl: QuestAiAnswerRepositoryImpl): QuestAiAnswerRepository

    @Binds
    @Singleton
    abstract fun bindBlockedUsersRepository(blockedUsersRepositoryImpl: BlockedUsersRepositoryImpl): BlockedUsersRepository

    @Binds
    @Singleton
    abstract fun bindQuestCommonRepository(questCommonRepositoryImpl: QuestCommonRepositoryImpl): QuestCommonRepository
}