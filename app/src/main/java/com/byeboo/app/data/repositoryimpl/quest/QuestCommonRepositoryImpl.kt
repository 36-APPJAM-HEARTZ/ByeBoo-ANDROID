package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCommonDataSource
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import javax.inject.Inject

class QuestCommonRepositoryImpl @Inject constructor(
    private val questCommonDataSource: QuestCommonDataSource,
) : QuestCommonRepository {

    private val cachedAnswers = mutableListOf<QuestAnswerModel>()

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
            val domainModel = response.data.toDomain()

            if (cursor == null) {
                cachedAnswers.clear()
            }
            cachedAnswers.addAll(domainModel.answers)

            domainModel
        }

    override suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: QuestCommonAnswerEditModel
    ): Result<Unit> =
        runCatching {
            questCommonDataSource.patchQuestCommonAnswer(
                answerId = answerId,
                request = request.toData()
            )
        }

    override fun getCachedMyAnswer(answerId: Long): QuestAnswerModel? {
        return cachedAnswers.find { it.answerId == answerId }
    }
}