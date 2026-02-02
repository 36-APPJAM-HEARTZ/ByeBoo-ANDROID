package com.byeboo.app.data.dto.response.offboarding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OffboardingJourneyResponseDto(
    @SerialName("inCompletedCount")
    val uncompletedCount: Int,
    @SerialName("inCompletedJourneys")
    val uncompletedJourneys: List<OffboardingJourneyDto>,
    @SerialName("completedCount")
    val completedCount: Int,
    @SerialName("completedJourneys")
    val completedJourneys: List<OffboardingJourneyDto>,
)

@Serializable
data class OffboardingJourneyDto(
    @SerialName("journey")
    val journey: String,
    @SerialName("style")
    val style: String,
)
