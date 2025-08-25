package com.byeboo.app.presentation.offboarding.model

data class JourneyCard(
    val journeyType: JourneyType,
    val status: JourneyStatus
)

enum class JourneyStatus {
    UNCOMPLETED,
    COMPLETED
}
