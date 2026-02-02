package com.byeboo.app.data.repositoryimpl.quest.recording

import com.byeboo.app.data.datasource.remote.quest.QuestDetailRemoteDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestDetailModel
import com.byeboo.app.domain.repository.quest.QuestDetailRecordingRepository
import javax.inject.Inject

class QuestDetailRecordingRepositoryImpl
@Inject
constructor(
    private val questDetailRemoteDataSource: QuestDetailRemoteDataSource
) : QuestDetailRecordingRepository {
    override suspend fun getQuestRecordingDetail(questId: Long): Result<QuestDetailModel> =
        runCatching {
            val response = questDetailRemoteDataSource.getQuestDetail(questId)
            response.data.toDomain()
        }
}
