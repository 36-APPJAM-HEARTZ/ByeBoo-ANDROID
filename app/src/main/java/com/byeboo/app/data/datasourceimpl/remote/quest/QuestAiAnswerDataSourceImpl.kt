package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestAiAnswerDataSource
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.quest.QuestAiAnswerResponseDto
import com.byeboo.app.data.service.quest.QuestAiAnswerService
import javax.inject.Inject

class QuestAiAnswerDataSourceImpl
    @Inject
    constructor(
        private val questAiAnswerService: QuestAiAnswerService,
    ) : QuestAiAnswerDataSource {
        override suspend fun postAiAnswer(questId: Long): NullableBaseResponse<QuestAiAnswerResponseDto> =
            questAiAnswerService.postAiAnswer(questId = questId)

        override suspend fun getAiAnswer(questId: Long): NullableBaseResponse<QuestAiAnswerResponseDto> =
            questAiAnswerService.getAiAnswer(questId = questId)
    }
