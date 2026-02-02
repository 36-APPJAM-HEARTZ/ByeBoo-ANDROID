package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestInProgressResponseDto(
    @SerialName("progressPeriod")
    val progressPeriod: Long,
    @SerialName("currentStep")
    val currentStep: Int,
    @SerialName("questOpenTime")
    val questOpenTime: String? = null,
    @SerialName("currentTime")
    val currentTime: String? = null,
    @SerialName("steps")
    val steps: List<QuestInProgressStepResponseDto>
)

@Serializable
data class QuestInProgressStepResponseDto(
    @SerialName("stepNumber")
    val stepNumber: Long,
    @SerialName("step")
    val step: String,
    @SerialName("quests")
    val quests: List<QuestInProgressQuestResponseDto>
)

@Serializable
data class QuestInProgressQuestResponseDto(
    @SerialName("questId")
    val questId: Long,
    @SerialName("question")
    val question: String,
    @SerialName("questStyle")
    val questStyle: String,
    @SerialName("questNumber")
    val questNumber: Long
)
