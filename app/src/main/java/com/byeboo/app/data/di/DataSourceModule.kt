package com.byeboo.app.data.di

import com.byeboo.app.data.datasource.remote.auth.UserRemoteDataSource
import com.byeboo.app.data.datasource.remote.offboarding.OffboardingJourneyDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestBehaviorAnswerDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestDetailRemoteDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestInProgressDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestRecordedDetailDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestRecordingDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestStateDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestTipDataSource
import com.byeboo.app.data.datasourceimpl.remote.auth.UserRemoteDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.offboarding.OffboardingJourneyDataSourceImpl
import com.byeboo.app.data.datasourceimpl.remote.quest.QuestBehaviorAnswerDataSourceImpl
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
    abstract fun bindQuestBehaviorAnswerDataSource(impl: QuestBehaviorAnswerDataSourceImpl): QuestBehaviorAnswerDataSource

    @Binds
    @Singleton
    abstract fun bindQuestRecordingDataSource(impl: QuestRecordingDataSourceImpl): QuestRecordingDataSource

    @Binds
    @Singleton
    abstract fun bindQuestRecordedDetailDataSource(impl: QuestRecordedDetailDataSourceImpl): QuestRecordedDetailDataSource

    @Binds
    @Singleton
    abstract fun bindOffboardingJourneyDataSource(impl: OffboardingJourneyDataSourceImpl): OffboardingJourneyDataSource
}
