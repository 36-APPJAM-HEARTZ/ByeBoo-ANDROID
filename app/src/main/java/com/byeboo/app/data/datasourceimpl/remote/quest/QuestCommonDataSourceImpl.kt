package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCommonDataSource
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.service.quest.QuestCommonService
import javax.inject.Inject

class QuestCommonDataSourceImpl @Inject constructor(
    private val questCommonService: QuestCommonService
) : QuestCommonDataSource {
    override suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonRequestDto
    ): NullableBaseResponse<Unit> =
        questCommonService.uploadQuestCommonAnswer(
            questId = questId,
            request = request
        )
}