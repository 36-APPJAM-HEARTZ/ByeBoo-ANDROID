package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestRecordedDetailResponseDto(
    @SerialName("question")
    val question: String,
    @SerialName("answer")
    val answer: String,
    @SerialName("imageKey")
    val imageKey: String? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("stepNumber")
    val stepNumber: Long,
    @SerialName("questNumber")
    val questNumber: Long,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("questEmotionState")
    val questEmotionState: String,
    @SerialName("emotionDescription")
    val emotionDescription: String,
)
