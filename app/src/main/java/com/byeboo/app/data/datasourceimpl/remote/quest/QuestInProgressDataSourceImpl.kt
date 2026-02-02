package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestInProgressDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestInProgressResponseDto
import com.byeboo.app.data.service.quest.QuestService
import javax.inject.Inject

class QuestInProgressDataSourceImpl
@Inject
constructor(
    private val questService: QuestService
) : QuestInProgressDataSource {
    override suspend fun getInProgressQuest(): BaseResponse<QuestInProgressResponseDto> = questService.getInProgressQuest()
}
