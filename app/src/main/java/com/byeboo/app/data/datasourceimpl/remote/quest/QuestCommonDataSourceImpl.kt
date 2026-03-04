package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestCommonDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestCommonEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.dto.response.quest.QuestMyCommonAnswerDto
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


    override suspend fun getQuestCommonMyAnswer(cursor: Long?): BaseResponse<QuestMyCommonAnswerDto> =
        questCommonService.getQuestMyCommonAnswer(
            cursor = cursor
        )

    override suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: QuestCommonEditRequestDto
    ): NullableBaseResponse<Unit> =
        questCommonService.patchQuestCommonAnswer(
            answerId = answerId,
            request = request
        )
}