package com.byeboo.app.presentation.quest.tip

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.QuestType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class QuestTipState(
    val questId: Long = 0,
    val questType: QuestType = QuestType.ACTIVE,
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val question: String = "",
    val tipStep: ImmutableList<Int> = persistentListOf(1, 2, 3),
    val tipAnswer: QuestTipAnswers = QuestTipAnswers("", "", ""),
)

data class QuestTipAnswers(
    val reason: String,
    val suggestion: String,
    val change: String,
)

sealed interface QuestTipSideEffect {
    data object NavigateToQuest : QuestTipSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : QuestTipSideEffect
}
