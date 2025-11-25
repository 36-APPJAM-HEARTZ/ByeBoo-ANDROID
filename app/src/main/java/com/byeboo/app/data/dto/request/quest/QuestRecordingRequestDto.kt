package com.byeboo.app.data.dto.request.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestRecordingRequestDto(
    @SerialName("answer")
    val answer: String,
    @SerialName("questEmotionState")
    val questEmotionState: String
)

@Serializable
data class QuestRecordingEditRequestDto(
    @SerialName("answer")
    val answer: String,
)
