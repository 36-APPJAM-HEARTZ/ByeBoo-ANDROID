package com.byeboo.app.presentation.offboarding

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.presentation.offboarding.model.JourneyCard
import com.byeboo.app.presentation.offboarding.model.JourneyStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class OffboardingJourneyState(
    val journeyCards: ImmutableList<JourneyCard> = persistentListOf()
) {
    val uncompletedCards: ImmutableList<JourneyCard>
        get() = journeyCards.filter { it.status == JourneyStatus.UNCOMPLETED }.toImmutableList()
    val completedCards: ImmutableList<JourneyCard>
        get() = journeyCards.filter { it.status == JourneyStatus.COMPLETED }.toImmutableList()
    val completedCount: Int get() = completedCards.size
    val uncompletedCount: Int get() = uncompletedCards.size
}

sealed interface OffboardingJourneySideEffect {
    data object NavigateUp : OffboardingJourneySideEffect

    data class NavigateToOffboardingQuestCompleted(
        val journey: QuestType
    ) : OffboardingJourneySideEffect

    data class ShowSnackBar(
        val message: String
    ) : OffboardingJourneySideEffect
}
