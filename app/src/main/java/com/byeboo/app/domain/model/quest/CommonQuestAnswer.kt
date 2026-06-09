package com.byeboo.app.domain.model.quest

import java.time.LocalDateTime

data class CommonQuestModel(
    val question: String,
    val answerCount: Long,
    val answers: List<CommonQuestAnswer>,
    val isAnswered: Boolean,
    val hasNext: Boolean,
    val nextCursor: Long?,
    val questId: Long,
)

data class CommonQuestAnswer(
    val heartCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val answerId: Long,
    val profileIcon: String,
    val writer: String,
    val writtenAt: LocalDateTime,
    val content: String,
    val writerId: Long,
)

data class QuestCommonAnswerRequestModel(
    val answer: String,
)

data class QuestCommonMyAnswerModel(
    val hasNext: Boolean,
    val nextCursor: Long?,
    val answers: List<QuestAnswerModel>,
)

data class QuestAnswerModel(
    val heartCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val question: String,
    val answerId: Long,
    val writtenAt: String,
    val content: String,
)

data class QuestAnswerDetailModel(
    val question: String,
    val answer: QuestAnswerDetailAnswerModel,
    val comments: List<QuestCommonDetailCommentModel>,
)

data class QuestAnswerDetailAnswerModel(
    val heartCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val writer: String,
    val writerId: Long,
    val profileIcon: String,
    val content: String,
    val writtenAt: LocalDateTime,
)

data class QuestCommonDetailCommentModel(
    val commentId: Long,
    val writer: String,
    val writerId: Long,
    val profileIcon: String,
    val content: String,
    val writtenAt: LocalDateTime,
)

data class QuestCommonAnswerEditModel(
    val answer: String,
)
