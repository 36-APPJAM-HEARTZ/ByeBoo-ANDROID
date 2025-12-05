package com.byeboo.app.presentation.quest.behavior

import android.net.Uri
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestWritingState

data class QuestBehaviorState(
    val stepNumber: Long = 0,
    val step: String = "",
    val questId: Long = 0,
    val questNumber: Long = 0,
    val question: String = "",
    val imageCount: Int = 0,
    val createdAt: String = java.time.LocalDate.now().toString(),
    val questAnswer: String = "",
    val imageUrl: String = "",
    val imageKey: String? = "",
    val questEmotionState: String = "",
    val emotionDescription: String = "",
    val isContentAvailable: Boolean = false,
    val contentState: QuestWritingState = QuestWritingState.Ready,
    val selectedEmotion: LargeTagType? = null,
    val showBottomSheet: Boolean = false,
    val selectedImageUri: Uri? = null,
    val showQuitModal: Boolean = false,
    val isUploading: Boolean = false,
    val isEditMode: Boolean = false,
    val originalAnswer: String = "",
    val isCompleteButtonEnabled: Boolean = false,
    val hasAnswerChanged: Boolean = false,
    val fromOffboarding: Boolean = false
)

sealed interface QuestBehaviorSideEffect {
    data object NavigateToQuest : QuestBehaviorSideEffect
    data class NavigateToQuestTip(val questId: Long, val questType: QuestType) : QuestBehaviorSideEffect
    data class NavigateToQuestBehaviorComplete(val questId: Long) : QuestBehaviorSideEffect
    data class NavigateToQuestReview(val questId: Long) : QuestBehaviorSideEffect
    data class CompleteAndClear(val questId: Long) : QuestBehaviorSideEffect
    data object NavigateUp: QuestBehaviorSideEffect
    data class ShowSnackBar(val message: String) : QuestBehaviorSideEffect
}
