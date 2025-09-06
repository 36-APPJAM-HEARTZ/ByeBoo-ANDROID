package com.byeboo.app.data.datasourceimpl.remote.offboarding

import com.byeboo.app.data.datasource.remote.offboarding.OffboardingNewJourneyDataSource
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.service.offboarding.OffboardingNewJourneyService
import javax.inject.Inject

class OffboardingNewJourneyDataSourceImpl @Inject constructor(
    private val offboardingNewJourneyService: OffboardingNewJourneyService
) : OffboardingNewJourneyDataSource {
    override suspend fun postOffboardingNewJourney(journey: String): NullableBaseResponse<Unit> {
        return offboardingNewJourneyService.postOffboardingNewJourney(journey)
    }
}
