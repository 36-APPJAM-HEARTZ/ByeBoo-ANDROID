package com.byeboo.app.data.mapper.quest

import com.byeboo.app.data.dto.request.quest.QuestCommonEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.dto.response.quest.CommentDetailDto
import com.byeboo.app.data.dto.response.quest.CommentRepliesResponseDto
import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto
import com.byeboo.app.data.dto.response.quest.QuestAnswerDto
import com.byeboo.app.data.dto.response.quest.QuestCommonAnswerDetailResponseDto
import com.byeboo.app.data.dto.response.quest.QuestCommonDetailAnswerDto
import com.byeboo.app.data.dto.response.quest.QuestCommonDetailCommentDto
import com.byeboo.app.data.dto.response.quest.QuestLikeResponseDto
import com.byeboo.app.data.dto.response.quest.QuestMyCommonAnswerResponseDto
import com.byeboo.app.data.dto.response.quest.ReplyItemDto
import com.byeboo.app.domain.model.quest.CommentDetailModel
import com.byeboo.app.domain.model.quest.CommentRepliesModel
import com.byeboo.app.domain.model.quest.CommentReplyModel
import com.byeboo.app.domain.model.quest.CommonQuestAnswer
import com.byeboo.app.domain.model.quest.CommonQuestModel
import com.byeboo.app.domain.model.quest.QuestAnswerDetailAnswerModel
import com.byeboo.app.domain.model.quest.QuestAnswerDetailModel
import com.byeboo.app.domain.model.quest.QuestAnswerModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestCommonDetailCommentModel
import com.byeboo.app.domain.model.quest.QuestCommonMyAnswerModel
import com.byeboo.app.domain.model.quest.QuestLikeModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun QuestCommonAnswerRequestModel.toData(): QuestCommonRequestDto =
    QuestCommonRequestDto(
        answer = this.answer,
    )

fun QuestMyCommonAnswerResponseDto.toDomain(): QuestCommonMyAnswerModel =
    QuestCommonMyAnswerModel(
        hasNext = this.hasNext,
        nextCursor = this.nextCursor,
        answers =
            this.answers.map {
                QuestAnswerModel(
                    heartCount = it.likeCount,
                    commentCount = it.commentCount,
                    isLiked = it.isLiked,
                    question = it.question,
                    answerId = it.answerId,
                    writtenAt = it.writtenAt.split("T")[0],
                    content = it.content,
                )
            },
    )

fun QuestCommonAnswerEditModel.toData(): QuestCommonEditRequestDto =
    QuestCommonEditRequestDto(
        answer = this.answer,
    )

fun CommonQuestResponseDto.toDomain(): CommonQuestModel =
    CommonQuestModel(
        question = this.question,
        answerCount = this.answerCount,
        answers = this.answers.map { it.toDomain() },
        isAnswered = this.isAnswered,
        hasNext = this.hasNext,
        nextCursor = this.nextCursor,
        questId = this.questId,
    )

fun QuestAnswerDto.toDomain(): CommonQuestAnswer =
    CommonQuestAnswer(
        answerId = this.answerId,
        heartCount = this.likeCount,
        commentCount = this.commentCount,
        isLiked = this.isLiked,
        profileIcon = this.profileIcon,
        writer = this.writer,
        writtenAt = LocalDateTime.parse(this.writtenAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        content = this.content,
        writerId = this.writerId,
    )

fun QuestCommonAnswerDetailResponseDto.toDomain(): QuestAnswerDetailModel =
    QuestAnswerDetailModel(
        question = this.question,
        answer = this.answer.toDomain(),
        comments = this.comments.map { it.toDomain() },
    )

fun QuestCommonDetailAnswerDto.toDomain(): QuestAnswerDetailAnswerModel =
    QuestAnswerDetailAnswerModel(
        heartCount = this.likeCount,
        commentCount = this.commentCount,
        isLiked = this.isLiked,
        writer = this.writer,
        writerId = this.writerId,
        profileIcon = this.profileIcon,
        content = this.content,
        writtenAt = this.writtenAt.toLocalDateTime(),
    )

fun QuestCommonDetailCommentDto.toDomain(): QuestCommonDetailCommentModel =
    QuestCommonDetailCommentModel(
        commentId = this.commentId,
        replyCount = this.replyCount,
        writer = this.writer,
        writerId = this.writerId,
        profileIcon = this.profileIcon,
        content = this.content,
        writtenAt = this.writtenAt.toLocalDateTime(),
    )

fun CommentRepliesResponseDto.toDomain(): CommentRepliesModel =
    CommentRepliesModel(
        totalCount = this.totalCount,
        comment = this.comment.toDomain(),
        replies = this.replies.map { it.toDomain() },
    )

fun CommentDetailDto.toDomain(): CommentDetailModel =
    CommentDetailModel(
        commentId = this.commentId,
        writerId = this.writerId,
        writer = this.writer,
        profileIcon = this.profileIcon,
        content = this.content,
        writtenAt = this.createdAt.toLocalDateTime(),
    )

fun ReplyItemDto.toDomain(): CommentReplyModel =
    CommentReplyModel(
        commentId = this.commentId,
        writerId = this.writerId,
        writer = this.writer,
        profileIcon = this.profileIcon,
        content = this.content,
        writtenAt = this.createdAt.toLocalDateTime(),
    )

fun QuestLikeResponseDto.toDomain(): QuestLikeModel =
    QuestLikeModel(
        heartCount = this.likeCount,
        isLiked = this.isLiked,
    )

private fun String.toLocalDateTime(): LocalDateTime =
    if (contains("T")) {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    } else {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay()
    }
