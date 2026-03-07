package com.byeboo.app.presentation.quest.aianswer

data class QuestAiAnswerState(
    val questAiAnswer: String = "",
    val isExistedAiAnswer: Boolean = false,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
)

sealed interface QuestAiAnswerSideEffect {
    data object NavigateToQuest : QuestAiAnswerSideEffect

    data object NavigateToOffboarding : QuestAiAnswerSideEffect
}
