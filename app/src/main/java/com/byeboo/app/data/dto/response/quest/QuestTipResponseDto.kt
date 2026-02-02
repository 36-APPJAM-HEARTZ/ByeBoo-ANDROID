package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestTipResponseDto(
    @SerialName("question")
    val question: String,
    @SerialName("step")
    val step: String,
    @SerialName("stepNumber")
    val stepNumber: Long,
    @SerialName("questNumber")
    val questNumber: Long,
    @SerialName("tips")
    val tips: List<Tip>,
)

@Serializable
data class Tip(
    @SerialName("tipStep")
    val tipStep: Int,
    @SerialName("tipAnswer")
    val tipAnswer: String,
)
