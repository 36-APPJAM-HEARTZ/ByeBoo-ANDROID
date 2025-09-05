package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestCompletedResponseDto

interface QuestCompletedDataSource {
    suspend fun getCompletedQuest(journey: String): BaseResponse<QuestCompletedResponseDto>
}
