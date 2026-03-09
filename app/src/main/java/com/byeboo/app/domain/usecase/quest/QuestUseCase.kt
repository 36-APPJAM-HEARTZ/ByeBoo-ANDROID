package com.byeboo.app.domain.usecase.quest

import com.byeboo.app.domain.model.quest.QuestDataModel
import com.byeboo.app.domain.repository.quest.QuestInProgressRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import javax.inject.Inject

class QuestUseCase
    @Inject
    constructor(
        private val questInProgressRepository: QuestInProgressRepository,
        private val questStateRepository: QuestStateRepository,
    ) {
        suspend operator fun invoke(): QuestDataModel {
            val result = questInProgressRepository.getInProgressQuest().getOrThrow()
            val journey = questStateRepository.getUserJourney().orEmpty()
            val questCompletedCount = questStateRepository.getQuestCount().getOrNull()?.count ?: 0L
            return QuestDataModel(
                inProgressQuest = result,
                journeyTitle = journey,
                questCompletedCount = questCompletedCount,
            )
        }
    }
