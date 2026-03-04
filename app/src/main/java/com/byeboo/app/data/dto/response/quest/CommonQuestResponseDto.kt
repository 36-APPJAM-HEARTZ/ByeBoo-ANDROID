package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonQuestResponseDto(
    @SerialName("question")
    val question: String,
    @SerialName("answerCount")
    val answerCount: Long,
    @SerialName("answers")
    val answers: List<QuestAnswerDto>,
    @SerialName("isAnswered")
    val isAnswered: Boolean,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursor")
    val nextCursor: Long,
    @SerialName("questId")
    val questId: Long,
)

@Serializable
data class QuestAnswerDto(
    @SerialName("answerId")
    val answerId: Long,
    @SerialName("writer")
    val writer: String,
    @SerialName("profileIcon")
    val profileIcon: String,
    @SerialName("writtenAt")
    val writtenAt: String,
    @SerialName("content")
    val content: String,
)
