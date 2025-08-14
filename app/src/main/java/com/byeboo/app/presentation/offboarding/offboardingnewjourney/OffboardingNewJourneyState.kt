package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import com.byeboo.app.domain.model.offboarding.JourneyType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class OffboardingNewJourneyState(
    val journeyCards: ImmutableList<JourneyCards> = persistentListOf()
) {
    val uncompleted: Int = 0
    val completed: Int = 0
    val uncompletedCards: ImmutableList<JourneyCards>
        get() = journeyCards.filter { it.status == JourneyStatus.UNCOMPLETED }.toImmutableList()
    val completedCards: ImmutableList<JourneyCards>
        get() = journeyCards.filter { it.status == JourneyStatus.COMPLETED }.toImmutableList()
}

data class JourneyCards(
    val journeyType: JourneyType,
    val status: JourneyStatus
)

enum class JourneyStatus {
    UNCOMPLETED,
    COMPLETED
}