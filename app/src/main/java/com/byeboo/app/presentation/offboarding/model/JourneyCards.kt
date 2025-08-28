package com.byeboo.app.presentation.offboarding.model

import com.byeboo.app.domain.model.offboarding.JourneyType

data class JourneyCards(
    val journeyType: JourneyType,
    val status: JourneyStatus
)

enum class JourneyStatus {
    UNCOMPLETED,
    COMPLETED
}
