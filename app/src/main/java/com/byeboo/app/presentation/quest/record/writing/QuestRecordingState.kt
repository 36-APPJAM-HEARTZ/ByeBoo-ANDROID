package com.byeboo.app.presentation.quest.record.writing

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.model.quest.QuestWritingState

@Immutable
data class QuestRecordingState(
    val questId: Long = 0,
    val step: String = "",
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val question: String = "",
    val questAnswer: String = "",
    val contentsState: QuestWritingState = QuestWritingState.Empty,
    val showQuitModal: Boolean = false,
    val showBottomSheet: Boolean = false,
    val selectedEmotion: EmotionChipType? = null,
    val isEditMode: Boolean = false,
    val originalAnswer: String = "",
    val fromOffboarding: Boolean = false,
    val showCompleteModal: Boolean = false,
) {
    val hasAnswerChanged: Boolean
        get() = questAnswer != originalAnswer

    val isCompleteButtonEnabled: Boolean get() {
        val isValid = QuestContentLengthValidator.validButton(questAnswer)
        return if (isEditMode) isValid && hasAnswerChanged else isValid
    }
}

sealed interface QuestRecordingSideEffect {
    data object NavigateToQuest : QuestRecordingSideEffect

    data class NavigateToQuestTip(
        val questId: Long,
        val questType: QuestType,
    ) : QuestRecordingSideEffect

    data class NavigateToQuestRecordingComplete(
        val questId: Long,
    ) : QuestRecordingSideEffect

    data class NavigateToQuestReview(
        val questId: Long,
    ) : QuestRecordingSideEffect

    data object NavigateUp : QuestRecordingSideEffect

    data class ShowSnackBar(
        val message: String,
    ) : QuestRecordingSideEffect
}
