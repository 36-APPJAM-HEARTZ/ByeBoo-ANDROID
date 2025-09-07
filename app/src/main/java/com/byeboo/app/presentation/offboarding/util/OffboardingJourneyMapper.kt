package com.byeboo.app.presentation.offboarding.util

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyModel
import com.byeboo.app.presentation.offboarding.OffboardingJourneyState
import com.byeboo.app.presentation.offboarding.model.JourneyCard
import com.byeboo.app.presentation.offboarding.model.JourneyStatus
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject


class OffboardingJourneyMapper @Inject constructor() {
    fun toUiState(model: OffboardingJourneyModel): OffboardingJourneyState {
        val journeyCardList = buildList {
            model.uncompletedCards.forEach { card ->
                add(
                    JourneyCard(
                        journeyType = QuestType.fromQuestStyle(card.style),
                        status = JourneyStatus.UNCOMPLETED
                    )
                )
            }

            model.completedCards.forEach { card ->
                add(
                    JourneyCard(
                        journeyType = QuestType.fromQuestStyle(card.style),
                        status = JourneyStatus.COMPLETED
                    )
                )
            }
        }.toImmutableList()

        return OffboardingJourneyState(journeyCards = journeyCardList)
    }
}
