package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.model.CommonAnswerModel

@Immutable
data class MyDetailAnswerState(
    val questQuestion: String = "",
    val answer: CommonAnswerModel? = null,
    val showBottomSheet: Boolean = false,
    val showDeleteModal: Boolean = false,
)

interface MyDetailAnswerSideEffect {
    data object NavigateUp : MyDetailAnswerSideEffect

    data object NavigateToQuestMyAnswers : MyDetailAnswerSideEffect

    data class NavigateToQuestCommonEdit(
        val answerId: Long,
        val question: String,
        val isEditMode: Boolean,
    ) : MyDetailAnswerSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : MyDetailAnswerSideEffect
}
