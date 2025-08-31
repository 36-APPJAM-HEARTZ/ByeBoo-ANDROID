package com.byeboo.app.domain.usecase

import com.byeboo.app.domain.model.quest.QuestData
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestInProgressRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

class QuestUseCase @Inject constructor(
    private val questInProgressRepository: QuestInProgressRepository,
    private val questStateRepository: QuestStateRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): QuestData {
        val result = questInProgressRepository.getInProgressQuest().getOrThrow()
        val journey = questStateRepository.getUserJourney() ?: ""
        val nickname = userRepository.getNickname().firstOrNull() ?: ""
        val questCompletedCount = questStateRepository.getQuestCount().getOrNull()?.count ?: 1L
        return QuestData(
            inProgressQuest = result,
            journeyTitle = journey,
            userNickname = nickname,
            questCompletedCount = questCompletedCount
        )
    }
}
