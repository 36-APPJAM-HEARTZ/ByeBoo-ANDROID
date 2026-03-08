package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestCommonEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.dto.response.quest.QuestMyCommonAnswerResponseDto

interface QuestCommonDataSource {
    suspend fun uploadQuestCommonAnswer(
        questId: Long,
        request: QuestCommonRequestDto,
    ): NullableBaseResponse<Unit>

    suspend fun getQuestCommonMyAnswer(cursor: Long?): BaseResponse<QuestMyCommonAnswerResponseDto>

    suspend fun patchQuestCommonAnswer(
        answerId: Long,
        request: QuestCommonEditRequestDto
    ): NullableBaseResponse<Unit>

    suspend fun deleteQuestCommonAnswer(
        answerId: Long,
    ): NullableBaseResponse<Unit>
}