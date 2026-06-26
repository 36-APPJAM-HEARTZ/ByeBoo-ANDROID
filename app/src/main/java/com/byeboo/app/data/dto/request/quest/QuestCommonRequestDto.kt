package com.byeboo.app.data.dto.request.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestCommonRequestDto(
    @SerialName("answer")
    val answer: String,
)

@Serializable
data class QuestCommonEditRequestDto(
    @SerialName("answer")
    val answer: String,
)

@Serializable
data class QuestCommonCommentRequestDto(
    @SerialName("content")
    val content: String,
    @SerialName("targetId")
    val targetId: Long,
)

@Serializable
data class
QuestCommentReplyRequestDto(
    @SerialName("content")
    val content: String,
)

@Serializable
data class ReportCommonQuestRequestDto(
    @SerialName("targetType")
    val targetType: String,
    @SerialName("targetId")
    val targetId: Long,
)

@Serializable
data class UpdateCommonQuestCommentRequestDto(
    @SerialName("content")
    val content: String,
)
