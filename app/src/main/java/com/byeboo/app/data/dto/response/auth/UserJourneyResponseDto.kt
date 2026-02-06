package com.byeboo.app.data.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserJourneyResponseDto(
    @SerialName("journey")
    val journey: String,
    @SerialName("description")
    val description: String,
)
