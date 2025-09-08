package com.byeboo.app.presentation.quest.record

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestWritingState

@Immutable
data class QuestRecordingState(
    val questId: Long = 0,
    val step: String = "",
    val stepNumber: Long = 0,
    val questNumber: Long = 0,
    val questQuestion: String = "",
    val questAnswer: String = "",
    val contentsState: QuestWritingState = QuestWritingState.Empty,
    val showQuitModal: Boolean = false,
    val showBottomSheet: Boolean = false,
    val isEmotionSelected: Boolean = false,
    val selectedEmotion: LargeTagType = LargeTagType.EMOTION_NEUTRAL
)

sealed interface QuestRecordingSideEffect {
    data object NavigateToQuest : QuestRecordingSideEffect
    data class NavigateToQuestTip(val questId: Long, val questType: QuestType) : QuestRecordingSideEffect
    data class NavigateToQuestRecordingComplete(val questId: Long) : QuestRecordingSideEffect
    data class ShowSnackBar(val message: String) : QuestRecordingSideEffect
}
