package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestCompletedResponseDto(
    @SerialName("progressPeriod")
    val progressPeriod: String,
    @SerialName("currentStep")
    val currentStep: Int?,
    @SerialName("steps")
    val steps: List<QuestCompletedStepResponseDto>,
)

@Serializable
data class QuestCompletedStepResponseDto(
    @SerialName("stepNumber")
    val stepNumber: Long,
    @SerialName("step")
    val step: String,
    @SerialName("quests")
    val quests: List<QuestCompletedQuestResponseDto>,
)

@Serializable
data class QuestCompletedQuestResponseDto(
    @SerialName("questId")
    val questId: Long,
    @SerialName("question")
    val question: String,
    @SerialName("questStyle")
    val questStyle: String,
    @SerialName("questNumber")
    val questNumber: Long,
)
