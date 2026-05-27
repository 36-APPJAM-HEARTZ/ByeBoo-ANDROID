package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestCommentReplyRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonCommentRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.dto.response.quest.CommentRepliesResponseDto
import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto
import com.byeboo.app.data.dto.response.quest.QuestCommonAnswerDetailResponseDto
import com.byeboo.app.data.dto.response.quest.QuestMyCommonAnswerResponseDto

interface QuestCommonDataSource {
    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonRequestDto,
    ): NullableBaseResponse<Unit>

    suspend fun getQuestCommonMyAnswer(cursor: Long?): BaseResponse<QuestMyCommonAnswerResponseDto>

    suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: QuestCommonEditRequestDto,
    ): NullableBaseResponse<Unit>

    suspend fun deleteQuestCommonAnswer(answerId: Long): NullableBaseResponse<Unit>

    suspend fun getCommonQuests(
        date: String,
        cursor: Long?,
        limit: Int,
    ): BaseResponse<CommonQuestResponseDto>

    suspend fun getQuestCommonAnswerDetail(answerId: Long): BaseResponse<QuestCommonAnswerDetailResponseDto>

    suspend fun updateBlockedUser(blockedUserId: Long): NullableBaseResponse<Unit>

    suspend fun reportCommonQuest(answerId: Long): NullableBaseResponse<Unit>

    suspend fun uploadComment(
        request: QuestCommonCommentRequestDto,
    ): NullableBaseResponse<Unit>

    suspend fun uploadCommentReply(
        commentId: Long,
        request: QuestCommentReplyRequestDto,
    ): NullableBaseResponse<Unit>

    suspend fun getCommentReplies(commentId: Long): BaseResponse<CommentRepliesResponseDto>
}
