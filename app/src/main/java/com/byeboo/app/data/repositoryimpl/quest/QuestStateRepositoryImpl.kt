package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.data.datasource.local.UserLocalDataSource
import com.byeboo.app.data.datasource.remote.quest.QuestStateDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestDialogue
import com.byeboo.app.domain.model.quest.QuestStateModel
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import javax.inject.Inject

class QuestStateRepositoryImpl @Inject constructor(
    private val questStateDataSource: QuestStateDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : QuestStateRepository {
    override suspend fun updateQuestState() {
        val response = questStateDataSource.updateQuestState()
        if (!response.success) {
            throw IllegalStateException(response.message)
        }
    }

    override suspend fun updateUserJourney(journey: String) {
        userLocalDataSource.saveJourney(journey)
    }

    override suspend fun updateUserJourneyStatus(journeyStatus: String) {
        userLocalDataSource.saveJourneyStatus(journeyStatus)
    }

    override suspend fun getUserJourney(): String? {
        return userLocalDataSource.getJourney()
    }

    override suspend fun getUserJourneyStatus(): String? {
        return userLocalDataSource.getJourneyStatus()
    }

    override suspend fun getQuestDialogue(): Result<QuestDialogue> {
        return runCatching {
            val response = questStateDataSource.getQuestDialogue()
            response.data.toDomain()
        }
    }

    override suspend fun setQuestStarted(started: Boolean) {
        userLocalDataSource.setQuestStarted(started)
    }

    override suspend fun isQuestStarted(): Boolean {
        return userLocalDataSource.isQuestStarted()
    }

    override suspend fun getQuestCount(): Result<QuestStateModel> {
        return runCatching {
            val response = questStateDataSource.getQuestCount()
            response.data.toDomain()
        }
    }
}
