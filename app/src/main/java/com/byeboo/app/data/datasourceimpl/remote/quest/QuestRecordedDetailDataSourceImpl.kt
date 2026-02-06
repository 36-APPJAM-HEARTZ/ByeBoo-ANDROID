package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestRecordedDetailDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestRecordedDetailResponseDto
import com.byeboo.app.data.service.quest.QuestRecordedDetailService
import javax.inject.Inject

class QuestRecordedDetailDataSourceImpl
    @Inject
    constructor(
        private val questRecordedDetailService: QuestRecordedDetailService,
    ) : QuestRecordedDetailDataSource {
        override suspend fun getQuestRecordedDetail(questId: Long): BaseResponse<QuestRecordedDetailResponseDto> =
            questRecordedDetailService.getQuestRecordedDetail(questId)
    }
