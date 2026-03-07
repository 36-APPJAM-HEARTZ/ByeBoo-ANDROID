package com.byeboo.app.presentation.quest.aianswer

import com.byeboo.app.core.model.quest.QuestType

data class QuestAiAnswerState(
    val questAiAnswer: String = "",
    val isExistedAiAnswer: Boolean = false,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
)

sealed interface QuestAiAnswerSideEffect {
    data object NavigateToQuest : QuestAiAnswerSideEffect

    data class NavigateToOffboardingQuest(
        val questType: QuestType,
    ) : QuestAiAnswerSideEffect
}
