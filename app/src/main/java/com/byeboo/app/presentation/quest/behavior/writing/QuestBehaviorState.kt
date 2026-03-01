package com.byeboo.app.presentation.quest.behavior.writing

import android.net.Uri
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestWritingState
import java.time.LocalDate

data class QuestBehaviorState(
    val stepNumber: Long = 0,
    val step: String = "",
    val questId: Long = 0,
    val questNumber: Long = 0,
    val question: String = "",
    val imageCount: Int = 0,
    val createdAt: String =
        LocalDate
            .now()
            .toString(),
    val questAnswer: String = "",
    val imageUrl: String = "",
    val imageKey: String? = "",
    val questEmotionState: String = "",
    val emotionDescription: String = "",
    val isContentAvailable: Boolean = false,
    val contentState: QuestWritingState = QuestWritingState.Ready,
    val selectedEmotion: EmotionChipType? = null,
    val showBottomSheet: Boolean = false,
    val selectedImageUri: Uri? = null,
    val showQuitModal: Boolean = false,
    val isUploading: Boolean = false,
    val isEditMode: Boolean = false,
    val originalAnswer: String = "",
    val fromOffboarding: Boolean = false,
    val showCompleteModal: Boolean = false,
) {
    val hasAnswerChanged: Boolean
        get() = questAnswer != originalAnswer

    val isCompleteButtonEnabled: Boolean get() {
        val hasImage = imageCount > 0

        return if (isEditMode) {
            val imageChanged = selectedImageUri != null
            hasAnswerChanged || imageChanged
        } else {
            hasImage
        }
    }
}

sealed interface QuestBehaviorSideEffect {
    data object NavigateToQuest : QuestBehaviorSideEffect

    data class NavigateToQuestTip(
        val questId: Long,
        val questType: QuestType,
    ) : QuestBehaviorSideEffect

    data class NavigateToQuestBehaviorComplete(
        val questId: Long,
    ) : QuestBehaviorSideEffect

    data class NavigateToQuestReview(
        val questId: Long,
    ) : QuestBehaviorSideEffect

    data class CompleteAndClear(
        val questId: Long,
    ) : QuestBehaviorSideEffect

    data object NavigateUp : QuestBehaviorSideEffect

    data class ShowSnackBar(
        val message: String,
        val iconType: CustomSnackBarType,
    ) : QuestBehaviorSideEffect
}
