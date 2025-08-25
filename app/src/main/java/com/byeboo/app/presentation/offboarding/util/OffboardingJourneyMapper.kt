package com.byeboo.app.presentation.offboarding.util

import com.byeboo.app.domain.model.offboarding.OffboardingJourneyModel
import com.byeboo.app.presentation.offboarding.OffboardingJourneyState
import com.byeboo.app.presentation.offboarding.model.JourneyCard
import com.byeboo.app.presentation.offboarding.model.JourneyStatus
import com.byeboo.app.presentation.offboarding.model.JourneyType
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject


class OffboardingJourneyMapper @Inject constructor() {
    private fun String.toJourneyType(): JourneyType =
        when (this) {
            "ACTIVE" -> JourneyType.ACTIVE
            "RECORDING" -> JourneyType.RECORDING
            else -> JourneyType.ACTIVE
        }

    fun toUiState(model: OffboardingJourneyModel): OffboardingJourneyState {
        val journeyCardList = buildList {
            model.uncompletedCards.forEach { card ->
                add(
                    JourneyCard(
                        journeyType = card.style.toJourneyType(),
                        status = JourneyStatus.UNCOMPLETED
                    )
                )
            }

            model.completedCards.forEach { card ->
                add(
                    JourneyCard(
                        journeyType = card.style.toJourneyType(),
                        status = JourneyStatus.COMPLETED
                    )
                )
            }
        }.toImmutableList()

        return OffboardingJourneyState(journeyCards = journeyCardList)
    }
}
