package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestBehaviorDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestBehaviorEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestBehaviorRequestDto
import com.byeboo.app.data.dto.request.quest.QuestSignedUrlRequestDto
import com.byeboo.app.data.dto.response.quest.QuestSingedUrlResponseDto
import com.byeboo.app.data.service.quest.QuestBehaviorService
import javax.inject.Inject
import okhttp3.RequestBody
import retrofit2.Response

class QuestBehaviorDataSourceImpl @Inject constructor(
    private val questBehaviorService: QuestBehaviorService
) : QuestBehaviorDataSource {

    override suspend fun requestQuestSignedUrl(request: QuestSignedUrlRequestDto): BaseResponse<QuestSingedUrlResponseDto> {
        return questBehaviorService.requestQuestSignedUrl(request = request)
    }

    override suspend fun uploadImageToSignedUrl(signedUrl: String, requestBody: RequestBody): Response<Unit> {
        return questBehaviorService.uploadImageToUrl(signedUrl, requestBody)
    }

    override suspend fun uploadQuestBehaviorAnswer(
        questId: Long,
        request: QuestBehaviorRequestDto
    ): NullableBaseResponse<Unit> {
        return questBehaviorService.uploadQuestAnswer(questId = questId, request = request)
    }

    override suspend fun updateQuestBehavior(
        questId: Long,
        request: QuestBehaviorEditRequestDto
    ): NullableBaseResponse<Unit> {
        return questBehaviorService.patchQuestBehavior(questId = questId, request = request)
    }
}
