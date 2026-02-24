package com.byeboo.app.presentation.quest.common.writing

import androidx.compose.runtime.Immutable
import com.byeboo.app.domain.model.quest.QuestWritingState

@Immutable
data class QuestCommonState(
    val question: String = "",
    val questAnswer: String = "",
    val contentsState: QuestWritingState = QuestWritingState.Empty,
    val isCompleteButtonEnabled: Boolean = false,
    val isEditMode: Boolean = false,
    val hasAnswerChanged: Boolean = false,
    val originalAnswer: String = "",
)

sealed interface QuestCommonSideEffect{
    data object NavigateToQuest

}