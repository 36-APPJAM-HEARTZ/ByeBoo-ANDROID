package com.byeboo.app.presentation.offboarding.util

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestCompletedModel
import com.byeboo.app.presentation.offboarding.model.QuestCompleted
import com.byeboo.app.presentation.offboarding.model.QuestCompletedGroup
import com.byeboo.app.presentation.offboarding.offboardingquestcompleted.QuestCompletedState
import com.byeboo.app.presentation.quest.model.QuestState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun QuestCompletedModel.toUiState(
    journey: QuestType,
    nickname: String = "하츠핑"
): QuestCompletedState {
    val groups: ImmutableList<QuestCompletedGroup> =
        steps.orEmpty().map { step ->
            val quests: ImmutableList<QuestCompleted> =
                step.quests.orEmpty().map { quest ->
                    QuestCompleted(
                        questId = quest.questId,
                        questNumber = quest.questNumber,
                        questQuestion = quest.question,
                        state = QuestState.Complete,
                        type = QuestType.fromQuestStyle(quest.questStyle)
                    )
                }.toImmutableList()

            QuestCompletedGroup(
                stepNumber = step.stepNumber,
                stepTitle = step.step,
                quests = quests
            )
        }.toImmutableList()

    return QuestCompletedState(
        questGroups = groups,
        progressPeriod = progressPeriod.orEmpty(),
        userName = nickname,
        questType = journey
    )
}
