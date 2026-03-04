package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.remote.quest.QuestAiAnswerDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestAiAnswerModel
import com.byeboo.app.domain.repository.quest.QuestAiAnswerRepository
import javax.inject.Inject

class QuestAiAnswerRepositoryImpl
    @Inject
    constructor(
        private val questAiAnswerDataSource: QuestAiAnswerDataSource,
    ) : QuestAiAnswerRepository {
        override suspend fun postAiAnswer(questId: Long): Result<QuestAiAnswerModel> =
            runCatching {
                questAiAnswerDataSource.postAiAnswer(questId = questId).data?.toDomain() ?: throw IllegalStateException()
            }

    override suspend fun getAiAnswer(questId: Long): Result<QuestAiAnswerModel> =
        runCatching{
            questAiAnswerDataSource.getAiAnswer(questId = questId).data?.toDomain() ?: throw IllegalStateException()
        }
    }
