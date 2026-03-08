package com.byeboo.app.data.service.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto
import com.byeboo.app.data.dto.response.quest.QuestCountResponseDto
import com.byeboo.app.data.dto.response.quest.QuestDialogueResponseDto
import com.byeboo.app.data.dto.response.quest.QuestInProgressResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface QuestService {
    // 최초 여정 상태 변경
    @PATCH("/api/v1/users/journey/start")
    suspend fun updateQuestStartState(): NullableBaseResponse<Unit>

    // 퀘스트 진행도
    @GET("/api/v1/users/count")
    suspend fun getQuestCount(): BaseResponse<QuestCountResponseDto>

    // 퀘스트 상태에 따른 캐릭터 대사
    @GET("/api/v1/users/character")
    suspend fun getQuestDialogue(): BaseResponse<QuestDialogueResponseDto>

    // 진행중인 전체 퀘스트 조회
    @GET("/api/v1/quests/all/progress")
    suspend fun getInProgressQuest(): BaseResponse<QuestInProgressResponseDto>

    // 날짜별 공통 퀘스트 전체 조회
    @GET("/api/v1/common-quests")
    suspend fun getCommonQuests(
        @Query("date") date: String,
        @Query("cursor") cursor: Long?,
        @Query("limit") limit: Int = 10,
    ): BaseResponse<CommonQuestResponseDto>
}
