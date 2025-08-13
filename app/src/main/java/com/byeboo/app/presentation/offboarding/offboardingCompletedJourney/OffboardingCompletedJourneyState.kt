package com.byeboo.app.presentation.offboarding.offboardingCompletedJourney

import com.byeboo.app.domain.model.offboarding.JourneyType
import com.byeboo.app.presentation.offboarding.offboardingnewjourney.JourneyStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class OffboardingCompletedJourneyState(
    val journeyCards: ImmutableList<JourneyCards> = persistentListOf()
) {
    val completed: Int = 0
    val completedCards: ImmutableList<JourneyCards>
        get() = journeyCards.filter { it.status == JourneyStatus.COMPLETED }.toImmutableList()
}

data class JourneyCards(
    val journeyType: JourneyType,
    val status: JourneyStatus
)