package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestMyCommonAnswerResponseDto(
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursor")
    val nextCursor: Long?,
    @SerialName("answers")
    val answers: List<QuestAnswerItemResponseDto>,
)

@Serializable
data class QuestAnswerItemResponseDto(
    @SerialName("question")
    val question: String,
    @SerialName("answerId")
    val answerId: Long,
    @SerialName("writtenAt")
    val writtenAt: String,
    @SerialName("content")
    val content: String,
)
