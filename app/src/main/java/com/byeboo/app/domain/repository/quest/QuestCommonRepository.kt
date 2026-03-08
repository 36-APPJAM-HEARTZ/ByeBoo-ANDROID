package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import kotlinx.coroutines.flow.StateFlow

interface QuestCommonRepository {
    val answersFlow: StateFlow<List<QuestAnswerModel>>

    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonAnswerRequestModel,
    ): Result<Unit>

    suspend fun getQuestCommonMyAnswer(): Result<QuestCommonMyAnswerModel>

    suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: QuestCommonAnswerEditModel,
    ): Result<Unit>

    suspend fun deleteQuestCommonAnswer(
        answerId: Long
    ): Result<Unit>

    fun getCachedMyAnswer(answerId: Long): QuestAnswerModel?
}
