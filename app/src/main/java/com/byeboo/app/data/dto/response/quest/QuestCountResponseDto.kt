package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestCountResponseDto(
    @SerialName("todayComplete")
    val todayComplete: Boolean?,
    @SerialName("userCurrentStatus")
    val userCurrentStatus: String,
    @SerialName("count")
    val count: Long
)