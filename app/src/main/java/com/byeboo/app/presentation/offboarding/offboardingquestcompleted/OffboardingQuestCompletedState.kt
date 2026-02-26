package com.byeboo.app.presentation.offboarding.offboardingquestcompleted

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.presentation.offboarding.model.QuestCompletedGroup
import com.byeboo.app.presentation.quest.model.Quest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class QuestCompletedState(
    val questGroups: ImmutableList<QuestCompletedGroup> = persistentListOf(),
    val progressPeriod: String = "",
    val userName: String = "하츠핑",
    val questType: QuestType = QuestType.RECORDING,
    val selectedQuest: Quest? = null,
)

sealed interface QuestCompletedSideEffect {
    data object NavigateUp : QuestCompletedSideEffect

    data class NavigateToOffboardingQuestReview(
        val questId: Long,
        val journey: QuestType,
    ) : QuestCompletedSideEffect

    data class ShowSnackBar(
        val message: String,
        val iconType: CustomSnackBarType,
    ) : QuestCompletedSideEffect
}
