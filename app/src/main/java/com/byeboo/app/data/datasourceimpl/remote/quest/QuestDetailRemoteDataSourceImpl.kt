package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestDetailRemoteDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.QuestDetailResponseDto
import com.byeboo.app.data.service.quest.QuestDetailService
import javax.inject.Inject

class QuestDetailRemoteDataSourceImpl
    @Inject
    constructor(
        private val questDetailService: QuestDetailService,
    ) : QuestDetailRemoteDataSource {
        override suspend fun getQuestDetail(questId: Long): BaseResponse<QuestDetailResponseDto> =
            questDetailService.getQuestDetail(questId)
    }
