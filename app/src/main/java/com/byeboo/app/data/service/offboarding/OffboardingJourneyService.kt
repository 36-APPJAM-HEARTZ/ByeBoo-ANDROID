package com.byeboo.app.data.service.offboarding

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.offboarding.OffboardingJourneyResponseDto
import retrofit2.http.GET

interface OffboardingJourneyService {
    @GET("/api/v1/quests/journey")
    suspend fun getOffboardingJourney(): BaseResponse<OffboardingJourneyResponseDto>
}
