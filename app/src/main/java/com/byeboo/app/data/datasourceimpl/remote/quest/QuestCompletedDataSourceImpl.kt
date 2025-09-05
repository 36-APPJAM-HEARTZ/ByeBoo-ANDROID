package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCompletedDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestCompletedResponseDto
import com.byeboo.app.data.service.quest.QuestService
import javax.inject.Inject

class QuestCompletedDataSourceImpl @Inject constructor(
    private val questService: QuestService
): QuestCompletedDataSource {
    override suspend fun getCompletedQuest(journey: String): BaseResponse<QuestCompletedResponseDto> {
        return questService.getCompletedQuest(journey)
    }
}
