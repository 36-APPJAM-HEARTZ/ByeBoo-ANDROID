package com.byeboo.app.domain.model.quest

import java.time.LocalDateTime

data class CommonQuestAnswer(
    val answerId: Long,
    val profileIcon: String,
    val writer: String,
    val writtenAt: LocalDateTime,
    val content: String,
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
    val question: String,
    val answerId: Long,
    val writtenAt: String,
    val content: String,
)

data class QuestCommonAnswerEditModel(
    val answer: String,
)
