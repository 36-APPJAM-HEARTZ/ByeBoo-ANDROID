package com.byeboo.app.domain.usecase

import com.byeboo.app.domain.model.quest.QuestData
import com.byeboo.app.domain.repository.quest.QuestInProgressRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import javax.inject.Inject

class QuestUseCase @Inject constructor(
    private val questInProgressRepository: QuestInProgressRepository,
    private val questStateRepository: QuestStateRepository,
) {
    suspend operator fun invoke(): QuestData {
        val result = questInProgressRepository.getInProgressQuest().getOrThrow()
        val journey = questStateRepository.getUserJourney() ?: ""
        return QuestData(
            inProgressQuest = result,
            journeyTitle = journey
        )
    }
}
