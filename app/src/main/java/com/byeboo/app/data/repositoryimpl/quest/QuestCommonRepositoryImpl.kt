package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCommonDataSource
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import javax.inject.Inject

class QuestCommonRepositoryImpl @Inject constructor(
    private val questCommonDataSource: QuestCommonDataSource,
) : QuestCommonRepository {
    override suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonAnswerRequestModel
    ): Result<Unit> =
        runCatching {
            questCommonDataSource.uploadQuestCommonAnswer(
                questId = questId,
                request = request.toData()
            )
        }

    override suspend fun getQuestCommonMyAnswer(cursor: Long?): Result<QuestCommonMyAnswerModel> =
        runCatching {
            val response = questCommonDataSource.getQuestCommonMyAnswer(cursor)
            response.data.toDomain()
        }
}