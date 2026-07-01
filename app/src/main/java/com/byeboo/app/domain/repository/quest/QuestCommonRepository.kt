package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.CommentRepliesModel
import com.byeboo.app.domain.model.quest.CommonQuestAnswerEditModel
import com.byeboo.app.domain.model.quest.CommonQuestAnswerRequestModel
import com.byeboo.app.domain.model.quest.CommonQuestCommentEditModel
import com.byeboo.app.domain.model.quest.CommonQuestModel
import com.byeboo.app.domain.model.quest.QuestAnswerDetailModel
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestLikeModel
import com.byeboo.app.domain.model.quest.QuestLikeUpdateModel
import com.byeboo.app.domain.model.quest.ReportCommentQuestModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface QuestCommonRepository {
    val answersFlow: StateFlow<List<QuestAnswerModel>>
    val answerSubmittedEvent: SharedFlow<Long>
    val refreshEvent: SharedFlow<Unit>
    val likeUpdatedEvent: SharedFlow<QuestLikeUpdateModel>

    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: CommonQuestAnswerRequestModel,
    ): Result<Unit>

    suspend fun refreshMyAnswers(): Result<Unit>

    suspend fun loadMyAnswers(): Result<Unit>

    suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: CommonQuestAnswerEditModel,
    ): Result<Unit>

    suspend fun deleteQuestCommonAnswer(answerId: Long): Result<Unit>

    suspend fun getCommonQuests(
        date: String,
        cursor: Long?,
        limit: Int,
    ): Result<CommonQuestModel>

    suspend fun getCommonQuestAnswerDetail(answerId: Long): Result<QuestAnswerDetailModel>

    suspend fun updateBlockedUser(blockedUserId: Long): Result<Unit>

    suspend fun reportCommonQuest(request: ReportCommentQuestModel): Result<Unit>

    suspend fun uploadComment(
        content: String,
        targetId: Long,
    ): Result<Unit>

    suspend fun getCommentReplies(commentId: Long): Result<CommentRepliesModel>

    suspend fun uploadCommentReply(
        commentId: Long,
        content: String,
    ): Result<Unit>

    fun getCachedMyAnswer(answerId: Long): QuestAnswerModel?

    suspend fun updateAnswerLike(answerId: Long): Result<QuestLikeModel>

    suspend fun deleteCommonQuestComment(commentId: Long): Result<Unit>

    suspend fun updateCommonQuestComment(
        commentId: Long,
        request: CommonQuestCommentEditModel,
    ): Result<Unit>
}
