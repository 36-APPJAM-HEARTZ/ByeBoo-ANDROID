package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestBehaviorAnswerDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestBehaviorAnswerRequestDto
import com.byeboo.app.data.dto.request.quest.QuestSignedUrlRequestDto
import com.byeboo.app.data.dto.response.quest.QuestSingedUrlResponseDto
import com.byeboo.app.data.service.quest.QuestBehaviorService
import javax.inject.Inject
import okhttp3.RequestBody
import retrofit2.Response

class QuestBehaviorAnswerDataSourceImpl @Inject constructor(
    private val questBehaviorService: QuestBehaviorService
) : QuestBehaviorAnswerDataSource {

    override suspend fun requestQuestSignedUrl(request: QuestSignedUrlRequestDto): BaseResponse<QuestSingedUrlResponseDto> {
        return questBehaviorService.requestQuestSignedUrl(request = request)
    }

    override suspend fun uploadImageToSignedUrl(signedUrl: String, requestBody: RequestBody): Response<Unit> {
        return questBehaviorService.uploadImageToUrl(signedUrl, requestBody)
    }

    override suspend fun uploadQuestBehaviorAnswer(
        questId: Long,
        request: QuestBehaviorAnswerRequestDto
    ): NullableBaseResponse<Unit> {
        return questBehaviorService.uploadQuestAnswer(questId = questId, request = request)
    }
}
