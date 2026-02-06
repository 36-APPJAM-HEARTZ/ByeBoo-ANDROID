package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestBehaviorEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestBehaviorRequestDto
import com.byeboo.app.data.dto.request.quest.QuestSignedUrlRequestDto
import com.byeboo.app.data.dto.response.quest.QuestSingedUrlResponseDto
import okhttp3.RequestBody
import retrofit2.Response

interface QuestBehaviorDataSource {
    suspend fun requestQuestSignedUrl(request: QuestSignedUrlRequestDto): BaseResponse<QuestSingedUrlResponseDto>

    suspend fun uploadImageToSignedUrl(
        signedUrl: String,
        requestBody: RequestBody,
    ): Response<Unit>

    suspend fun uploadQuestBehaviorAnswer(
        questId: Long,
        request: QuestBehaviorRequestDto,
    ): NullableBaseResponse<Unit>

    suspend fun updateQuestBehavior(
        questId: Long,
        request: QuestBehaviorEditRequestDto,
    ): NullableBaseResponse<Unit>
}
