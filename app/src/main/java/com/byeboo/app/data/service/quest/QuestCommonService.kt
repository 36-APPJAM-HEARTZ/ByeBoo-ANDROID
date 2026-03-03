package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestCommonService {
    @POST("/api/v1/common-quests/{questId}")
    suspend fun uploadQuestCommonAnswer(
        @Path("questId") questId: Long,
        @Body request: QuestCommonRequestDto,
    ) : NullableBaseResponse<Unit>

}