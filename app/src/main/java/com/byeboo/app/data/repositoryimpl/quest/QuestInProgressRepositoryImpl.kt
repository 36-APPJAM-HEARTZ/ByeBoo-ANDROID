package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestInProgressDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestInProgressModel
import com.byeboo.app.domain.repository.quest.QuestInProgressRepository
import javax.inject.Inject

class QuestInProgressRepositoryImpl
@Inject
constructor(
    private val questInProgressDataSource: QuestInProgressDataSource
) : QuestInProgressRepository {
    override suspend fun getInProgressQuest(): Result<QuestInProgressModel> =
        runCatching {
            val response = questInProgressDataSource.getInProgressQuest()
            response.data.toDomain()
        }
}
