package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCommonDataSource
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
import com.byeboo.app.data.service.quest.QuestCommonService
import javax.inject.Inject

class QuestCommonDataSourceImpl
    @Inject
    constructor(
        private val questCommonService: QuestCommonService,
    ) : QuestCommonDataSource {
        override suspend fun uploadQuestCommonAnswer(
            questId: Long,
            request: QuestCommonRequestDto,
        ): NullableBaseResponse<Unit> =
            questCommonService.uploadQuestCommonAnswer(
                questId = questId,
                request = request,
            )

        override suspend fun getQuestCommonMyAnswer(cursor: Long?): BaseResponse<QuestMyCommonAnswerResponseDto> =
            questCommonService.getQuestMyCommonAnswer(
                cursor = cursor,
            )

        override suspend fun patchQuestCommonAnswer(
            answerId: Long,
            request: QuestCommonEditRequestDto,
        ): NullableBaseResponse<Unit> =
            questCommonService.patchQuestCommonAnswer(
                answerId = answerId,
                request = request,
            )

        override suspend fun deleteQuestCommonAnswer(answerId: Long): NullableBaseResponse<Unit> =
            questCommonService.deleteQuestCommonAnswer(
                answerId = answerId,
            )

        override suspend fun getCommonQuests(
            date: String,
            cursor: Long?,
            limit: Int,
        ): BaseResponse<CommonQuestResponseDto> = questCommonService.getCommonQuests(date, cursor, limit)

        override suspend fun getQuestCommonAnswerDetail(answerId: Long): BaseResponse<QuestCommonAnswerDetailResponseDto> =
            questCommonService.getQuestCommonAnswerDetail(answerId)

        override suspend fun updateBlockedUser(blockedUserId: Long): NullableBaseResponse<Unit> =
            questCommonService.updateBlockedUser(blockedUserId)

        override suspend fun reportCommonQuest(answerId: Long): NullableBaseResponse<Unit> = questCommonService.reportCommonQuest(answerId)

        override suspend fun uploadComment(request: QuestCommonCommentRequestDto): NullableBaseResponse<Unit> =
            questCommonService.uploadComment(request)

        override suspend fun uploadCommentReply(
            commentId: Long,
            request: QuestCommentReplyRequestDto,
        ): NullableBaseResponse<Unit> = questCommonService.uploadCommentReply(commentId, request)

        override suspend fun getCommentReplies(commentId: Long): BaseResponse<CommentRepliesResponseDto> =
            questCommonService.getCommentReplies(commentId)
    }
