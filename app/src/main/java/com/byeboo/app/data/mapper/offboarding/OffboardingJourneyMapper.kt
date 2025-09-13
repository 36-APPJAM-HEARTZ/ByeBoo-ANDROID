package com.byeboo.app.data.mapper.offboarding

import com.byeboo.app.data.dto.response.offboarding.OffboardingJourneyDto
import com.byeboo.app.data.dto.response.offboarding.OffboardingJourneyResponseDto
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyCard
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyModel

fun OffboardingJourneyResponseDto.toDomain(): OffboardingJourneyModel {
    return OffboardingJourneyModel(
        uncompletedCount = this.uncompletedCount,
        uncompletedCards = uncompletedJourneys.map { it.toDomain() },
        completedCount = this.completedCount,
        completedCards = completedJourneys.map { it.toDomain() }
    )
}

fun OffboardingJourneyDto.toDomain(): OffboardingJourneyCard {
    return OffboardingJourneyCard(
        journey = this.journey,
        style = this.style
    )
}
