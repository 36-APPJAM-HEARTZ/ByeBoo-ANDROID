package com.byeboo.app.presentation.offboarding.model

import com.byeboo.app.core.model.quest.QuestType

data class JourneyCard(
    val journeyType: QuestType,
    val status: JourneyStatus
)

enum class JourneyStatus {
    UNCOMPLETED,
    COMPLETED
}
