package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.CommonQuestModel

interface CommonQuestRepository {
    suspend fun getCommonQuests(
        date: String,
        cursor: Long?,
        limit: Int,
    ): Result<CommonQuestModel>
}
