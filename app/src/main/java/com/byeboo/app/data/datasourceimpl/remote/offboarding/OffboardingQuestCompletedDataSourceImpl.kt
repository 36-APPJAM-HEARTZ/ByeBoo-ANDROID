package com.byeboo.app.data.datasourceimpl.remote.offboarding

import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.data.datasource.remote.offboarding.OffboardingQuestCompletedDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestCompletedResponseDto
import com.byeboo.app.data.service.offboarding.OffboardingQuestCompletedService
import javax.inject.Inject

class OffboardingQuestCompletedDataSourceImpl
    @Inject
    constructor(
        private val offboardingService: OffboardingQuestCompletedService,
    ) : OffboardingQuestCompletedDataSource {
        override suspend fun getCompletedQuest(journey: JourneyType): BaseResponse<QuestCompletedResponseDto> =
            offboardingService.getCompletedQuest(journey.journeyType)
    }
