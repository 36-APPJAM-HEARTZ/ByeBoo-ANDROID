package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCompletedDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestCompletedModel
import com.byeboo.app.domain.repository.quest.QuestCompletedRepository
import javax.inject.Inject

class QuestCompletedRepositoryImpl @Inject constructor(
    private val questCompletedDataSource: QuestCompletedDataSource
): QuestCompletedRepository {
    override suspend fun getCompletedQuest(journey: String): Result<QuestCompletedModel> {
        return runCatching {
            val response = questCompletedDataSource.getCompletedQuest(journey)
            response.data.toDomain()
        }
    }
}
