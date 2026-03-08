package com.byeboo.app.data.di

import com.byeboo.app.data.datasource.remote.NewJourneyDataSource
import com.byeboo.app.data.datasource.remote.auth.AuthRemoteDataSource
import com.byeboo.app.data.datasource.remote.auth.UserRemoteDataSource
import com.byeboo.app.data.datasource.remote.fcm.FcmRemoteDataSource
import com.byeboo.app.data.datasource.remote.mypage.BlockedUsersDataSource
import com.byeboo.app.data.datasource.remote.offboarding.OffboardingJourneyDataSource
import com.byeboo.app.data.datasource.remote.offboarding.OffboardingQuestCompletedDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestAiAnswerDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestBehaviorDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestDetailRemoteDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestInProgressDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestRecordedDetailDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestRecordingDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestStateDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestTipDataSource
import com.byeboo.app.data.datasourceimpl.remote.NewJourneyDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.auth.AuthRemoteDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.auth.UserRemoteDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.fcm.FcmRemoteDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.mypage.BlockedUsersDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.offboarding.OffboardingJourneyDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.offboarding.OffboardingQuestCompletedDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestAiAnswerDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestBehaviorDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestDetailRemoteDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestInProgressDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestRecordedDetailDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestRecordingDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestStateDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestTipDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsQuestStateDataSource(impl: QuestStateDataSourceImpl): QuestStateDataSource

    @Binds
    @Singleton
    abstract fun bindsQuestInProgressDataSource(impl: QuestInProgressDataSourceImpl): QuestInProgressDataSource

    @Binds
    @Singleton
    abstract fun bindQuestDetailRemoteDataSource(impl: QuestDetailRemoteDataSourceImpl): QuestDetailRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsQuestTipDataSource(impl: QuestTipDataSourceImpl): QuestTipDataSource

    @Binds
    @Singleton
    abstract fun bindQuestBehaviorAnswerDataSource(impl: QuestBehaviorDataSourceImpl): QuestBehaviorDataSource

    @Binds
    @Singleton
    abstract fun bindQuestRecordingDataSource(impl: QuestRecordingDataSourceImpl): QuestRecordingDataSource

    @Binds
    @Singleton
    abstract fun bindQuestRecordedDetailDataSource(impl: QuestRecordedDetailDataSourceImpl): QuestRecordedDetailDataSource

    @Binds
    @Singleton
    abstract fun bindOffboardingJourneyDataSource(impl: OffboardingJourneyDataSourceImpl): OffboardingJourneyDataSource

    @Binds
    @Singleton
    abstract fun bindOffboardingNewJourneyDataSource(impl: NewJourneyDataSourceImpl): NewJourneyDataSource

    @Binds
    @Singleton
    abstract fun bindsOffboardingQuestCompletedDataSource(
        impl: OffboardingQuestCompletedDataSourceImpl,
    ): OffboardingQuestCompletedDataSource

    @Binds
    @Singleton
    abstract fun bindsFcmRemoteDataSource(impl: FcmRemoteDataSourceImpl): FcmRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsBlockedUsersDataSource(impl: BlockedUsersDataSourceImpl): BlockedUsersDataSource

    @Binds
    @Singleton
    abstract fun bindsQuestAiAnswerDataSource(impl: QuestAiAnswerDataSourceImpl): QuestAiAnswerDataSource
}
