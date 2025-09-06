package com.byeboo.app.presentation.offboarding.model

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyType

data class JourneyCard(
    val category: OffboardingJourneyType,
    val journeyType: QuestType,
    val status: JourneyStatus
)

enum class JourneyStatus {
    UNCOMPLETED,
    COMPLETED
}
