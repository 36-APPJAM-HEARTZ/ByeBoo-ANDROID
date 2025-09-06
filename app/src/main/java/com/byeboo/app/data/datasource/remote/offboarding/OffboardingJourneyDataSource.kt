package com.byeboo.app.data.datasource.remote.offboarding

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.offboarding.OffboardingJourneyResponseDto

interface OffboardingJourneyDataSource {
    suspend fun getOffboardingJourney(): BaseResponse<OffboardingJourneyResponseDto>
}
