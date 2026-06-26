package com.byeboo.app.presentation.quest.review.common.personal

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.model.MyAnswerModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MyAnswerState(
    val userName: String = "",
    val answers: ImmutableList<MyAnswerModel> = persistentListOf(),
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface MyAnswerSideEffect {
    data object NavigateUp : MyAnswerSideEffect

    data object NavigateToQuest : MyAnswerSideEffect

    data class NavigateToQuestCommonAnswer(
        val answerId: Long,
    ) : MyAnswerSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : MyAnswerSideEffect
}
