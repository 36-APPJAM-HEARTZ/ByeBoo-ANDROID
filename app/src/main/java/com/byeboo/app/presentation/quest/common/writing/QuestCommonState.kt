package com.byeboo.app.presentation.quest.common.writing

import androidx.compose.runtime.Immutable
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.model.quest.QuestWritingState

@Immutable
data class QuestCommonState(
    val question: String = "",
    val questAnswer: String = "",
    val originalAnswer: String = "",
    val isEditMode: Boolean = false,
    val contentsState: QuestWritingState = QuestWritingState.Empty,
) {
    val hasAnswerChanged: Boolean
        get() = questAnswer != originalAnswer

    val isCompleteButtonEnabled: Boolean get() {
        val isValid = QuestContentLengthValidator.validButton(questAnswer)
        return if (isEditMode) isValid && hasAnswerChanged else isValid
    }
}

sealed interface QuestCommonSideEffect{
    data object NavigateToQuest

}