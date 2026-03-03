package com.byeboo.app.data.dto.request.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestCommonRequestDto(
    @SerialName("answer")
    val answer: String
)