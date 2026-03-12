package com.byeboo.app.presentation.offboarding.model

import com.byeboo.app.core.model.quest.JourneyType

data class JourneyCard(
    val journeyType: JourneyType,
    val status: JourneyStatus,
)

enum class JourneyStatus {
    UNCOMPLETED,
    COMPLETED,
}
