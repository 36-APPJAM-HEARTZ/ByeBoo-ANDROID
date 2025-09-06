package com.byeboo.app.data.service.offboarding

import com.byeboo.app.data.dto.base.NullableBaseResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface OffboardingNewJourneyService {
    @POST("/api/v1/quests/journey")
    suspend fun postOffboardingNewJourney(
        @Query("journey") journey: String
    ): NullableBaseResponse<Unit>
}
