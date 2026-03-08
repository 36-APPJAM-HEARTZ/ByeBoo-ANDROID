package com.byeboo.app.presentation.quest.common.writing

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.model.quest.QuestWritingState

@Immutable
data class QuestCommonState(
    val questId: Long? = 0,
    val answerId: Long? = 0,
    val question: String = "",
    val questAnswer: String = "",
    val originalAnswer: String = "",
    val isEditMode: Boolean = false,
    val showCompleteModal: Boolean = false,
    val showQuitModal: Boolean = false,
    val contentsState: QuestWritingState = QuestWritingState.Empty,
) {
    val hasAnswerChanged: Boolean
        get() = questAnswer != originalAnswer

    val isCompleteButtonEnabled: Boolean get() {
        val isValid = QuestContentLengthValidator.validButton(questAnswer)
        return if (isEditMode) isValid && hasAnswerChanged else isValid
    }
}

sealed interface QuestCommonSideEffect {
    data object NavigateToQuest : QuestCommonSideEffect

    data object NavigateToUp : QuestCommonSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : QuestCommonSideEffect
}
