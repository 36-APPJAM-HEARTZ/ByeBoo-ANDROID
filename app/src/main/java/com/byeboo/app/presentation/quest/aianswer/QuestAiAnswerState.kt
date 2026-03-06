package com.byeboo.app.presentation.quest.aianswer

data class QuestAiAnswerState(
    val questAiAnswer: String,
)

sealed interface QuestAiAnswerSideEffect {
    data object NavigateToQuest : QuestAiAnswerSideEffect
}
