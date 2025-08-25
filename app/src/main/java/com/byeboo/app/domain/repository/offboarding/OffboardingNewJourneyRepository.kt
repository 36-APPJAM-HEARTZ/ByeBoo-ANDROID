package com.byeboo.app.domain.repository.offboarding

interface OffboardingNewJourneyRepository {
    suspend fun postOffboardingNewJourney(journey: String): Result <Unit>
}
