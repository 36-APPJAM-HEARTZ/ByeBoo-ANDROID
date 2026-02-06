package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestDetailService {
    @GET("/api/v1/quests/{questId}")
    suspend fun getQuestDetail(
        @Path("questId") questId: Long,
    ): BaseResponse<QuestDetailResponseDto>
}
