package com.byeboo.app.data.repositoryimpl.offboarding

import com.byeboo.app.data.datasource.remote.offboarding.OffboardingJourneyDataSource
import com.byeboo.app.data.mapper.offboarding.toDomain
import com.byeboo.app.domain.model.offboarding.OffboardingJourneyModel
import com.byeboo.app.domain.repository.offboarding.OffboardingJourneyRepository
import javax.inject.Inject

class OffboardingJourneyRepositoryImpl @Inject constructor(
    private val offboardingJourneyDataSource: OffboardingJourneyDataSource
) : OffboardingJourneyRepository {
    override suspend fun getOffboardingJourney(): Result<OffboardingJourneyModel> = runCatching {
        val response = offboardingJourneyDataSource.getOffboardingJourney()
        response.data.toDomain()
    }
}
