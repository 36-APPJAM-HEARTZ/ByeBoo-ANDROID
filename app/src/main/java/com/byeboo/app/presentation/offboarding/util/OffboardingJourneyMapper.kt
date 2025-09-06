package com.byeboo.app.presentation.offboarding.util

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyModel
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyType
import com.byeboo.app.presentation.offboarding.OffboardingJourneyState
import com.byeboo.app.presentation.offboarding.model.JourneyCard
import com.byeboo.app.presentation.offboarding.model.JourneyStatus
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject


class OffboardingJourneyMapper @Inject constructor() {
    private fun String.toJourneyType(): QuestType =
        when (this) {
            "ACTIVE" -> QuestType.ACTIVE
            "RECORDING" -> QuestType.RECORDING
            else -> QuestType.ACTIVE
        }

    private fun String.toCategory(): OffboardingJourneyType =
        OffboardingJourneyType.fromDisplayName(this)

    fun toUiState(model: OffboardingJourneyModel): OffboardingJourneyState {
        val journeyCardList = buildList {
            model.uncompletedCards.forEach { card ->
                add(
                    JourneyCard(
                        category = card.journey.toCategory(),
                        journeyType = card.style.toJourneyType(),
                        status = JourneyStatus.UNCOMPLETED
                    )
                )
            }

            model.completedCards.forEach { card ->
                add(
                    JourneyCard(
                        category = card.journey.toCategory(),
                        journeyType = card.style.toJourneyType(),
                        status = JourneyStatus.COMPLETED
                    )
                )
            }
        }.toImmutableList()

        return OffboardingJourneyState(journeyCards = journeyCardList)
    }
}
