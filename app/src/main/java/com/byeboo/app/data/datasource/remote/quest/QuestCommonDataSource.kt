package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto

interface QuestCommonDataSource {
    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonRequestDto,
    ): NullableBaseResponse<Unit>
}