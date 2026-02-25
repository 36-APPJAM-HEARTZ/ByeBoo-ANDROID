package com.byeboo.app.presentation.quest.start

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.QuestType

@Immutable
data class QuestStartState(
    val nickname: String = "하츠핑",
    val journeyName: String = "감정 직면",
    val questType: QuestType? = null,
)

sealed interface QuestStartSideEffect {
    data object NavigateToQuest : QuestStartSideEffect

    data object NavigateToHome : QuestStartSideEffect

    data class ShowSnackBar(
        val message: String,
        val iconType: CustomSnackBarType
    ) : QuestStartSideEffect
}
