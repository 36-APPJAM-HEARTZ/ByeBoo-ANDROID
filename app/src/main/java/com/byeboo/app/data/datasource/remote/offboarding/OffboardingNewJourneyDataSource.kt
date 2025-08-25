package com.byeboo.app.data.datasource.remote.offboarding

import com.byeboo.app.data.dto.base.NullableBaseResponse

interface OffboardingNewJourneyDataSource {
    suspend fun postOffboardingNewJourney(journey: String): NullableBaseResponse<Unit>
}
