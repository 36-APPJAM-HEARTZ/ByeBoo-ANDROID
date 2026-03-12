package com.byeboo.app.presentation.quest.start

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.JourneyType

@Immutable
data class QuestStartState(
    val nickname: String = "하츠핑",
    val journeyName: String = "재회 준비",
    val journeyType: JourneyType? = null,
)

sealed interface QuestStartSideEffect {
    data object NavigateToQuest : QuestStartSideEffect

    data object NavigateToHome : QuestStartSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : QuestStartSideEffect
}
