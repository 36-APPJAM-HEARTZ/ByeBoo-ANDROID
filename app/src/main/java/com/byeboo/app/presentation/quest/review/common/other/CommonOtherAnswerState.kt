package com.byeboo.app.presentation.quest.review.common.other

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.model.CommonAnswerModel

data class CommonAnswerState(
    val createdAt: String = "",
    val questQuestion: String = "",
    val answer: CommonAnswerModel =
        CommonAnswerModel(
            answerId = 0L,
            writer = "",
            profileIconRes = 0,
            displayTime = "",
            content = "",
        ),
    val showBottomSheet: Boolean = false,
)

sealed interface CommonAnswerSideEffect {
    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : CommonAnswerSideEffect

    data object NavigateToQuest : CommonAnswerSideEffect
}
