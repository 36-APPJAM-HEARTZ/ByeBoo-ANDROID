package com.byeboo.app.presentation.quest.review.common.other

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import java.time.LocalDate

data class CommonAnswerState(
    val createdAt: String = LocalDate.now().toString(),
    val questQuestion: String = "",
    val answer: CommonAnswerModel? = null,
    val showBottomSheet: Boolean = false,
)

sealed interface CommonAnswerSideEffect {
    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : CommonAnswerSideEffect

    data object NavigateToQuest : CommonAnswerSideEffect
}
