package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestAiAnswerModel

interface QuestAiAnswerRepository {
    suspend fun postAiAnswer(questId: Long): Result<QuestAiAnswerModel>
    suspend fun getAiAnswer(questId: Long): Result<QuestAiAnswerModel>
}
