package com.byeboo.app.domain.repository.offboarding

import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.domain.model.quest.QuestCompletedModel

interface OffboardingQuestCompletedRepository {
    suspend fun getCompletedQuest(journey: JourneyType): Result<QuestCompletedModel>
}
