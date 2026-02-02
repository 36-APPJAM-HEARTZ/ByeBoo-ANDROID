package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestRecordedDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestRecordedDetailService {
    @GET("/api/v1/quests/answer/{questId}")
    suspend fun getQuestRecordedDetail(
        @Path("questId") questId: Long,
    ): BaseResponse<QuestRecordedDetailResponseDto>
}
