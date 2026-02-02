package com.byeboo.app.data.repositoryimpl.quest.behavior

import com.byeboo.app.data.datasource.remote.quest.QuestDetailRemoteDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestDetailModel
import com.byeboo.app.domain.repository.quest.QuestDetailBehaviorRepository
import javax.inject.Inject

class QuestDetailBehaviorRepositoryImpl
    @Inject
    constructor(
        private val questDetailRemoteDataSource: QuestDetailRemoteDataSource,
    ) : QuestDetailBehaviorRepository {
        override suspend fun getQuestBehaviorDetail(questId: Long): Result<QuestDetailModel> =
            runCatching {
                val response = questDetailRemoteDataSource.getQuestDetail(questId)
                response.data.toDomain()
            }
    }
