package com.byeboo.app.data.datasource.remote.quest

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.quest.CommonQuestResponseDto

interface CommonQuestDataSource {
    suspend fun getCommonQuests(
        date: String,
        cursor: Long?,
        limit: Int,
    ): BaseResponse<CommonQuestResponseDto>
}
