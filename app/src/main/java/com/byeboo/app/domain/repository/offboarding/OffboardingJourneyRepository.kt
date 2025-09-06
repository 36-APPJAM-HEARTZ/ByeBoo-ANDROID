package com.byeboo.app.domain.repository.offboarding

import com.byeboo.app.domain.model.offboarding.OffboardingJourneyModel

interface OffboardingJourneyRepository {
    suspend fun getOffboardingJourney(): Result<OffboardingJourneyModel>
}
