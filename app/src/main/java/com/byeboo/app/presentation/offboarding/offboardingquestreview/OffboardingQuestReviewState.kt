package com.byeboo.app.presentation.offboarding.offboardingquestreview

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.presentation.quest.navigation.AiAnswerEntryPoint
import java.time.LocalDate

data class OffboardingQuestReviewState(
    val isLoading: Boolean = true,
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val createdAt: String = LocalDate.now().toString(),
    val question: String = "",
    val answer: String = "",
    val imageKey: String? = null,
    val imageUrl: String? = null,
    val questEmotionState: String = "",
    val emotionDescription: String = "",
    val selectedEmotion: EmotionChipType = EmotionChipType.EMOTION_NEUTRAL,
    val questType: QuestType = QuestType.RECORDING,
    val isExistedAiAnswer: Boolean = false,
)

sealed interface OffboardingQuestReviewSideEffect {
    data class NavigateToOffboardingQuestCompleted(
        val journey: QuestType,
    ) : OffboardingQuestReviewSideEffect

    data class NavigateToQuestRecordingEdit(
        val questId: Long,
        val isEditMode: Boolean,
        val fromOffboarding: Boolean,
    ) : OffboardingQuestReviewSideEffect

    data class NavigateToQuestBehaviorEdit(
        val questId: Long,
        val isEditMode: Boolean,
        val fromOffboarding: Boolean,
        val imageKey: String,
    ) : OffboardingQuestReviewSideEffect

    data class NavigateToQuestAiAnswer(
        val questId: Long,
        val isExistedAiAnswer: Boolean,
        val aiAnswerEntryPoint: AiAnswerEntryPoint,
    ) : OffboardingQuestReviewSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : OffboardingQuestReviewSideEffect
}
