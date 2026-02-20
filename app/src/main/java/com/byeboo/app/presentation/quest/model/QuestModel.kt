package com.byeboo.app.presentation.quest.model

import com.byeboo.app.core.model.quest.QuestType
import kotlinx.collections.immutable.ImmutableList

data class QuestGroup(
    val stepNumber: Long,
    val stepTitle: String,
    val quests: ImmutableList<Quest>,
)

data class Quest(
    val questId: Long = 0,
    val questNumber: Long = 0,
    val questQuestion: String = "",
    val state: QuestState = QuestState.Available,
    val type: QuestType = QuestType.RECORDING,
)

data class CommonAnswerModel(
    val answerId: Long,
    val writer: String,
    val profileIconRes: Int,
    val displayTime: String,
    val content: String,
)
