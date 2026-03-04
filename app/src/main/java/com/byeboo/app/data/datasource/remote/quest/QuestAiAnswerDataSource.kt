package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.quest.QuestAiAnswerResponseDto

interface QuestAiAnswerDataSource {
    suspend fun postAiAnswer(questId: Long): NullableBaseResponse<QuestAiAnswerResponseDto>
}
