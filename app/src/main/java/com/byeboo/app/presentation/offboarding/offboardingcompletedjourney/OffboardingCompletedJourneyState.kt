package com.byeboo.app.presentation.offboarding.offboardingcompletedjourney

import com.byeboo.app.presentation.offboarding.model.JourneyCards
import com.byeboo.app.presentation.offboarding.model.JourneyStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class OffboardingCompletedJourneyState(
    val journeyCards: ImmutableList<JourneyCards> = persistentListOf()
) {
    val completedCards: ImmutableList<JourneyCards>
        get() = journeyCards.filter { it.status == JourneyStatus.COMPLETED }.toImmutableList()
    val completed: Int get() = completedCards.size
}
