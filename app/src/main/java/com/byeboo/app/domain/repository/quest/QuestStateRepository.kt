package com.byeboo.app.domain.repository.quest

import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.model.quest.QuestDialogue
import com.byeboo.app.domain.model.quest.QuestStateModel
import kotlinx.coroutines.flow.Flow

interface QuestStateRepository {
    suspend fun updateQuestStartState(): Result<Unit>

    suspend fun updateUserJourney(journey: String)

    suspend fun updateUserJourneyStatus(journeyStatus: JourneyStatusType)

    suspend fun getUserJourney(): String?

    fun getUserJourneyStatus(): Flow<JourneyStatusType>

    suspend fun getQuestDialogue(): Result<QuestDialogue>

    suspend fun getQuestCount(): Result<QuestStateModel>

    suspend fun setQuestStarted(started: Boolean)
}
