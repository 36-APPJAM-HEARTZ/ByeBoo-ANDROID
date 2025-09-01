package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.quest.QuestDialogue
import com.byeboo.app.domain.model.quest.QuestStateModel

interface QuestStateRepository {
    suspend fun updateQuestState()
    suspend fun updateUserJourney(journey: String)
    suspend fun updateUserJourneyStatus(journeyStatus: String)
    suspend fun getUserJourney(): String?
    suspend fun getUserJourneyStatus(): String?
    suspend fun getQuestDialogue(): Result<QuestDialogue>
    suspend fun getQuestCount(): Result<QuestStateModel>
    suspend fun isQuestStarted(): Boolean
    suspend fun setQuestStarted(started: Boolean)
}
