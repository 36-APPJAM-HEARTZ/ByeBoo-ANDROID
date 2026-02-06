package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestTipResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestTipService {
    @GET("/api/v1/quests/{questId}/tip")
    suspend fun getQuestTip(
        @Path("questId") questId: Long,
    ): BaseResponse<QuestTipResponseDto>
}
