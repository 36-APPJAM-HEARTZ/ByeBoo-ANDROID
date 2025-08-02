package com.byeboo.app.presentation.quest.start

import androidx.compose.runtime.Immutable

@Immutable
data class QuestStartState(
    val nickname: String? = "하츠핑",
    val journeyName: String = ""
)

sealed interface QuestStartSideEffect {
    data object NavigateToQuest : QuestStartSideEffect
    data object NavigateToHome : QuestStartSideEffect
}
