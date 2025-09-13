package com.byeboo.app.domain.repository.offboarding

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestCompletedModel

interface OffboardingQuestCompletedRepository {
    suspend fun getCompletedQuest(journey: QuestType): Result<QuestCompletedModel>
}
