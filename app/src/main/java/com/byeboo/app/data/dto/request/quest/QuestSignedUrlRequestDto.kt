package com.byeboo.app.data.dto.request.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestSignedUrlRequestDto(
    @SerialName("contentType")
    val contentType: String,
    @SerialName("imageKey")
    val imageKey: String,
)
