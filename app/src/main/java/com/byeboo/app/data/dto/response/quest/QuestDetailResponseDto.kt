package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestDetailResponseDto(
    @SerialName("step")
    val step: String,
    @SerialName("stepNumber")
    val stepNumber: Long,
    @SerialName("questNumber")
    val questNumber: Long,
    @SerialName("questStyle")
    val questStyle: String,
    @SerialName("question")
    val question: String,
)
