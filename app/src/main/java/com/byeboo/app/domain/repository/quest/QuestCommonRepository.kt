package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel


interface QuestCommonRepository {
    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonAnswerRequestModel,
    ): Result<Unit>
}