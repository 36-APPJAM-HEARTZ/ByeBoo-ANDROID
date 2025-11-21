package com.byeboo.app.presentation.quest.review

import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import java.time.LocalDate

data class QuestReviewState(
    val questId: Long = 0,
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val createdAt: String = LocalDate.now().toString(),
    val question: String = "",
    val answer: String = "",
    val imageUrl: String? = null,
    val questEmotionState: String = "",
    val emotionDescription: String = "",
    val selectedEmotion: LargeTagType = LargeTagType.EMOTION_NEUTRAL,
    val questType: QuestType = QuestType.RECORDING,
)

sealed interface QuestReviewSideEffect {
    data object NavigateToQuest : QuestReviewSideEffect
    data class NavigateToQuestRecording(val questId: Long, val isEditMode: Boolean) : QuestReviewSideEffect
    data class NavigateToQuestBehavior(val questId: Long, val isEditMode: Boolean) : QuestReviewSideEffect
    data class ShowSnackBar(val message: String) : QuestReviewSideEffect
}
