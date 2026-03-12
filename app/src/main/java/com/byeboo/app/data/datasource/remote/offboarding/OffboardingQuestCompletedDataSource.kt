package com.byeboo.app.data.datasource.remote.offboarding

import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestCompletedResponseDto

interface OffboardingQuestCompletedDataSource {
    suspend fun getCompletedQuest(journey: JourneyType): BaseResponse<QuestCompletedResponseDto>
}
