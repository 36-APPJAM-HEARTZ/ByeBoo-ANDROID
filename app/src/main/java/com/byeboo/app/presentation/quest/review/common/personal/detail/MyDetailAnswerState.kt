package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.model.MyAnswerModel

@Immutable
data class MyDetailAnswerState(
    val answer: MyAnswerModel =
        MyAnswerModel(
            answerId = 0L,
            question = "",
            writtenAt = "",
            content = "",
            isLiked = false,
            heartCount = 0,
            commentCount = 0,
        ),
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
