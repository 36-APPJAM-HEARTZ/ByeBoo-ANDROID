package com.byeboo.app.presentation.quest.complete

import com.byeboo.app.presentation.quest.model.Quest
import com.byeboo.app.presentation.quest.model.QuestCompletedGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class QuestCompletedState(
    val questGroups: ImmutableList<QuestCompletedGroup> = persistentListOf(),
    val progressPeriod: String = "",
    val userName: String = "하츠핑",
    val journeyTitle: String = "감정 직면",
    val selectedQuest: Quest? = null,
)

sealed interface QuestCompletedSideEffect {
    data object NavigateToOffboardingCompletedJourney : QuestCompletedSideEffect
    data class NavigateToQuestReview(val questId: Long) : QuestCompletedSideEffect
}
