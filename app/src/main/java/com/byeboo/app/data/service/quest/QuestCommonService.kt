package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.quest.QuestCommonEditRequestDto
import com.byeboo.app.data.dto.request.quest.QuestCommonRequestDto
import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto
import com.byeboo.app.data.dto.response.quest.QuestCommonAnswerDetailResponseDto
import com.byeboo.app.data.dto.response.quest.QuestMyCommonAnswerResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestCommonService {
    @POST("/api/v1/common-quests/{questId}")
    suspend fun uploadQuestCommonAnswer(
        @Path("questId") questId: Long,
        @Body request: QuestCommonRequestDto,
    ): NullableBaseResponse<Unit>

    @GET("/api/v1/users/me/common-quests")
    suspend fun getQuestMyCommonAnswer(
        @Query("cursor") cursor: Long?,
        @Query("limit") limit: Int = 10,
    ): BaseResponse<QuestMyCommonAnswerResponseDto>

    @PATCH("/api/v1/common-quests/{answerId}")
    suspend fun patchQuestCommonAnswer(
        @Path("answerId") answerId: Long,
        @Body request: QuestCommonEditRequestDto,
    ): NullableBaseResponse<Unit>

    @DELETE("/api/v1/common-quests/{answerId}")
    suspend fun deleteQuestCommonAnswer(
        @Path("answerId") answerId: Long,
    ): NullableBaseResponse<Unit>

    @GET("/api/v2/common-quests")
    suspend fun getCommonQuests(
        @Query("date") date: String,
        @Query("cursor") cursor: Long?,
        @Query("limit") limit: Int = 10,
    ): BaseResponse<CommonQuestResponseDto>

    @GET("/api/v2/common-quests/{answerId}")
    suspend fun getQuestCommonAnswerDetail(
        @Path("answerId") answerId: Long,
    ): BaseResponse<QuestCommonAnswerDetailResponseDto>

    @POST("/api/v1/blocks/{blockedUserId}")
    suspend fun updateBlockedUser(
        @Path("blockedUserId") blockedUserId: Long,
    ): NullableBaseResponse<Unit>

    @POST("/api/v1/reports/common-quests/{answerId}")
    suspend fun reportCommonQuest(
        @Path("answerId") answerId: Long,
    ): NullableBaseResponse<Unit>
}
