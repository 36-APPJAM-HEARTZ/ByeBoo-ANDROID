package com.byeboo.app.data.repositoryimpl.quest

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.data.datasource.remote.offboarding.OffboardingQuestCompletedDataSource
import com.byeboo.app.data.mapper.quest.toDomain
import com.byeboo.app.domain.model.quest.QuestCompletedModel
import com.byeboo.app.domain.repository.offboarding.OffboardingQuestCompletedRepository
import javax.inject.Inject

class OffboardingQuestCompletedRepositoryImpl @Inject constructor(
    private val offboardingQuestCompletedDataSource: OffboardingQuestCompletedDataSource
) : OffboardingQuestCompletedRepository {
    override suspend fun getCompletedQuest(journey: QuestType): Result<QuestCompletedModel> {
        return runCatching {
            val response = offboardingQuestCompletedDataSource.getCompletedQuest(journey)
            response.data.toDomain()
        }
    }
}
