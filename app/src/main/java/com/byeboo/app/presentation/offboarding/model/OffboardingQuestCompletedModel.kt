package com.byeboo.app.presentation.offboarding.model

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.presentation.quest.model.QuestState
import kotlinx.collections.immutable.ImmutableList

data class QuestCompletedGroup(
    val stepNumber: Long,
    val stepTitle: String,
    val quests: ImmutableList<QuestCompleted>
)

data class QuestCompleted(
    val questId: Long = 0,
    val questNumber: Long = 0,
    val questQuestion: String = "",
    val state: QuestState = QuestState.Complete,
    val type: QuestType = QuestType.RECORDING
)
