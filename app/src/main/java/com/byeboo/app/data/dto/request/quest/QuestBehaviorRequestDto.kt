package com.byeboo.app.data.dto.request.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestBehaviorRequestDto(
    @SerialName("answer")
    val answer: String? = null,
    @SerialName("questEmotionState")
    val questEmotionState: String,
    @SerialName("imageKey")
    val imageKey: String
)

@Serializable
data class QuestBehaviorEditRequestDto(
    @SerialName("answer")
    val answer: String? = null,
    @SerialName("imageKey")
    val imageKey: String
)
