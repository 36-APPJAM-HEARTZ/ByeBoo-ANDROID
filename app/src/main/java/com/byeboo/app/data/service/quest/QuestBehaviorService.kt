package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestBehaviorAnswerRequestDto
import com.byeboo.app.data.dto.request.quest.QuestSignedUrlRequestDto
import com.byeboo.app.data.dto.response.quest.QuestSingedUrlResponseDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface QuestBehaviorService {

    @POST("/api/v1/quests/images/signed-url")
    suspend fun requestQuestSignedUrl(
        @Body request: QuestSignedUrlRequestDto
    ): BaseResponse<QuestSingedUrlResponseDto>

    @PUT
    suspend fun uploadImageToUrl(
        @Url presignedUrl: String,
        @Body requestBody: RequestBody
    ): Response<Unit>

    @POST("/api/v1/quests/{questId}/active")
    suspend fun uploadQuestAnswer(
        @Path("questId") questId: Long,
        @Body request: QuestBehaviorAnswerRequestDto
    ): NullableBaseResponse<Unit>
}
