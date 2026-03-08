package com.byeboo.app.presentation.quest.aianswer

import com.byeboo.app.core.designsystem.type.CustomSnackBarType

data class QuestAiAnswerState(
    val questAiAnswer: String = "",
    val isExistedAiAnswer: Boolean = false,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
)

sealed interface QuestAiAnswerSideEffect {
    data object NavigateToQuest : QuestAiAnswerSideEffect

    data object NavigateUp : QuestAiAnswerSideEffect
    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : QuestAiAnswerSideEffect
}
