package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.CommonQuestModel
import com.byeboo.app.domain.model.quest.QuestAnswerDetailModel
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface QuestCommonRepository {
    val answersFlow: StateFlow<List<QuestAnswerModel>>
    val answerSubmittedEvent: SharedFlow<Long>
    val refreshEvent: SharedFlow<Unit>

    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonAnswerRequestModel,
    ): Result<Unit>

    suspend fun getQuestCommonMyAnswer(cursor: Long? = null): Result<QuestCommonMyAnswerModel>

    suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: QuestCommonAnswerEditModel,
    ): Result<Unit>

    suspend fun deleteQuestCommonAnswer(answerId: Long): Result<Unit>

    suspend fun getCommonQuests(
        date: String,
        cursor: Long?,
        limit: Int,
    ): Result<CommonQuestModel>

    suspend fun getCommonQuestAnswerDetail(answerId: Long): Result<QuestAnswerDetailModel>

    suspend fun updateBlockedUser(blockedUserId: Long): Result<Unit>

    suspend fun reportCommonQuest(answerId: Long): Result<Unit>

    fun getCachedMyAnswer(answerId: Long): QuestAnswerModel?


}
