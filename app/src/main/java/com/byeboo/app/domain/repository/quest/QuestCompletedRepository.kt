package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestCompletedModel

interface QuestCompletedRepository {
    suspend fun getCompletedQuest(journey: String): Result<QuestCompletedModel>
}
