package com.byeboo.app.presentation.quest.model

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.QuestType

sealed class QuestState {
    data object Available : QuestState()

    data object Complete : QuestState()

    data object Locked : QuestState()

    data class TimerLocked(
        val remainTime: Long,
    ) : QuestState()
}

sealed interface QuestSideEffect {
    data class NavigateToQuestTip(
        val questId: Long,
        val questType: QuestType,
    ) : QuestSideEffect

    data class NavigateToQuestRecording(
        val questId: Long,
    ) : QuestSideEffect

    data class NavigateToQuestBehavior(
        val questId: Long,
    ) : QuestSideEffect

    data class NavigateToQuestCommonWriting(
        val questId: Long,
    ) : QuestSideEffect

    data class NavigateToQuestReview(
        val questId: Long,
    ) : QuestSideEffect

    data object NavigateToQuestMyAnswers : QuestSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : QuestSideEffect
}

enum class QuestTab {
    MY_JOURNEY,
    COMMON_JOURNEY,
}
