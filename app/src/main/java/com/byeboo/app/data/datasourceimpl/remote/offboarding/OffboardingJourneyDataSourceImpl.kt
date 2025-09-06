package com.byeboo.app.data.datasourceimpl.remote.offboarding

import com.byeboo.app.data.datasource.remote.offboarding.OffboardingJourneyDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.offboarding.OffboardingJourneyResponseDto
import com.byeboo.app.data.service.offboarding.OffboardingJourneyService
import javax.inject.Inject

class OffboardingJourneyDataSourceImpl @Inject constructor(
    private val offboardingJourneyService: OffboardingJourneyService
) : OffboardingJourneyDataSource {
    override suspend fun getOffboardingJourney(): BaseResponse<OffboardingJourneyResponseDto> {
        return offboardingJourneyService.getOffboardingJourney()
    }
}
