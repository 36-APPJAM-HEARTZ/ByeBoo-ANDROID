package com.byeboo.app.data.repositoryimpl.offboarding

import com.byeboo.app.data.datasource.remote.offboarding.OffboardingNewJourneyDataSource
import com.byeboo.app.domain.repository.offboarding.OffboardingNewJourneyRepository
import javax.inject.Inject

class OffboardingNewJourneyRepositoryImpl @Inject constructor(
    private val offboardingNewJourneyDataSource: OffboardingNewJourneyDataSource
) : OffboardingNewJourneyRepository {
    override suspend fun postOffboardingNewJourney(journey: String): Result<Unit> = runCatching {
        val response = offboardingNewJourneyDataSource.postOffboardingNewJourney(journey)
        if (response.success) {
            Unit
        } else {
            throw IllegalStateException(response.message)
        }
    }
}
