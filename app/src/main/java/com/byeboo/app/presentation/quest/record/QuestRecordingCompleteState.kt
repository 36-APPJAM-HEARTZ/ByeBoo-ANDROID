package com.byeboo.app.presentation.quest.record

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.LargeTagType

@Immutable
data class QuestRecordingCompleteState(
    val questId: Long = 0,
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val createdAt: String = java.time.LocalDate.now().toString(),
    val question: String = "",
    val answer: String = "",
    val questEmotionState: String = "",
    val emotionDescription: String = "",
    val selectedEmotion: LargeTagType = LargeTagType.EMOTION_NEUTRAL
)

sealed interface QuestRecordingCompleteSideEffect {
    data object NavigateToQuest : QuestRecordingCompleteSideEffect
    data object NavigateToOffboardingCompletedGuide : QuestRecordingCompleteSideEffect
}
