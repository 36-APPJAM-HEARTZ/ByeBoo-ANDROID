package com.byeboo.app.data.service.offboarding

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestCompletedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OffboardingQuestCompletedService {
    @GET("/api/v1/quests/all/completed")
    suspend fun getCompletedQuest(
        @Query("journey") journey: String
    ): BaseResponse<QuestCompletedResponseDto>
}
