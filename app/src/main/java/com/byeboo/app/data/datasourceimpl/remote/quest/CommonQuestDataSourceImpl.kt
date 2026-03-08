package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.CommonQuestDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto
import com.byeboo.app.data.service.quest.QuestService
import javax.inject.Inject

class CommonQuestDataSourceImpl
    @Inject
    constructor(
        private val questService: QuestService,
    ) : CommonQuestDataSource {
        override suspend fun getCommonQuests(
            date: String,
            cursor: Long?,
            limit: Int,
        ): BaseResponse<CommonQuestResponseDto> = questService.getCommonQuests(date, cursor, limit)
    }
