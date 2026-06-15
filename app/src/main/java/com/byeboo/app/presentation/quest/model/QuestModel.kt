package com.byeboo.app.presentation.quest.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
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

@Immutable
data class CommonAnswerModel(
    val heartCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val answerId: Long,
    val writerId: Long,
    val writer: String,
    @DrawableRes val profileIconRes: Int,
    val displayTime: String,
    val content: String,
)

@Immutable
data class MyAnswerModel(
    val answerId: Long,
    val question: String,
    val writtenAt: String,
    val content: String,
    val heartCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
)
