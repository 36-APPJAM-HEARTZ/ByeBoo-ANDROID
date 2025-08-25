package com.byeboo.app.domain.model.offboarding

data class OffboardingJourneyModel(
    val uncompletedCount: Int,
    val uncompletedCards: List<OffboardingJourneyCard>,
    val completedCount: Int,
    val completedCards: List<OffboardingJourneyCard>
)

data class OffboardingJourneyCard (
    val journey: String,
    val style: String
)
