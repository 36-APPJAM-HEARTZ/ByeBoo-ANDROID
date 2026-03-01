package com.byeboo.app.presentation.quest.behavior.complete

import android.net.Uri
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.type.EmotionChipType

data class QuestBehaviorCompleteState(
    val questId: Long = 0,
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val question: String = "",
    val questAnswer: String = "",
    val createdAt: String =
        java.time.LocalDate
            .now()
            .toString(),
    val imageUrl: String = "",
    val selectedImageUri: Uri? = null,
    val emotionDescription: String = "",
    val selectedEmotion: EmotionChipType = EmotionChipType.EMOTION_NEUTRAL,
)

sealed interface QuestBehaviorCompleteSideEffect {
    data object NavigateToQuest : QuestBehaviorCompleteSideEffect

    data object NavigateToOffboardingCompletedGuide : QuestBehaviorCompleteSideEffect

    data object ShowInAppReview : QuestBehaviorCompleteSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : QuestBehaviorCompleteSideEffect
}
