package com.byeboo.app.domain.model.quest

import java.time.LocalDateTime

data class CommonQuestModel(
    val question: String,
    val answerCount: Long,
    val answers: List<CommonQuestAnswer>,
    val isAnswered: Boolean,
    val hasNext: Boolean,
    val nextCursor: Long,
    val questId: Long,
)

data class CommonQuestAnswer(
    val answerId: Long,
    val profileIcon: String,
    val writer: String,
    val writtenAt: LocalDateTime,
    val content: String,
)
