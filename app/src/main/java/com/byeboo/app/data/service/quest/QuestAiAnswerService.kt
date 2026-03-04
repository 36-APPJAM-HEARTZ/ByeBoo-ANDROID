package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.quest.QuestAiAnswerResponseDto
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestAiAnswerService {
    @POST("/api/v1/quests/{questId}/ai-answer")
    suspend fun postAiAnswer(
        @Path("questId") questId: Long,
    ): NullableBaseResponse<QuestAiAnswerResponseDto>
}
