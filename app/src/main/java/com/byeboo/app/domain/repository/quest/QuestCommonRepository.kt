package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import com.byeboo.app.presentation.quest.navigation.QuestCommonAnswer


interface QuestCommonRepository {
    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonAnswerRequestModel,
    ): Result<Unit>

    suspend fun getQuestCommonMyAnswer(
        cursor: Long?,
    ): Result<QuestCommonMyAnswerModel>
}
