package com.byeboo.app.data.dto.response.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestSingedUrlResponseDto(
    @SerialName("signedUrl")
    val signedUrl: String,
)
