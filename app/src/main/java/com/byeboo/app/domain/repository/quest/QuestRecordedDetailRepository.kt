package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestRecordedDetailModel
import kotlinx.coroutines.flow.Flow

interface QuestRecordedDetailRepository {
    suspend fun getQuestRecordedDetail(questId: Long): Result<QuestRecordedDetailModel>

    fun observeQuestRecordedDetail(questId: Long): Flow<QuestRecordedDetailModel>
}
