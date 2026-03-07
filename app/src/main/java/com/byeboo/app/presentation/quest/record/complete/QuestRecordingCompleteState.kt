package com.byeboo.app.presentation.quest.record.complete

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.presentation.quest.navigation.AiAnswerOrigin
import java.time.LocalDate

@Immutable
data class QuestRecordingCompleteState(
    val questId: Long = 0,
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val createdAt: String =
        LocalDate
            .now()
            .toString(),
    val question: String = "",
    val answer: String = "",
    val emotionDescription: String = "",
    val selectedEmotion: EmotionChipType = EmotionChipType.EMOTION_NEUTRAL,
    val isExistedAiAnswer: Boolean = false,
)

sealed interface QuestRecordingCompleteSideEffect {
    data object NavigateToQuest : QuestRecordingCompleteSideEffect

    data object NavigateToOffboardingCompletedGuide : QuestRecordingCompleteSideEffect

    data class NavigateToQuestAiAnswer(
        val questId: Long,
        val isExistedAiAnswer: Boolean,
        val aiAnswerOrigin: AiAnswerOrigin,
    ) : QuestRecordingCompleteSideEffect

    data object ShowInAppReview : QuestRecordingCompleteSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : QuestRecordingCompleteSideEffect
}
