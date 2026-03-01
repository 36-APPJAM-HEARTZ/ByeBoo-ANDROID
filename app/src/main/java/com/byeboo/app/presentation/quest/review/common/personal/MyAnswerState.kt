package com.byeboo.app.presentation.quest.review.common.personal

import androidx.compose.runtime.Immutable
import com.byeboo.app.presentation.quest.model.MyAnswerModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MyAnswerState(
    val answers: ImmutableList<MyAnswerModel> = persistentListOf(),
    val showBottomSheet: Boolean = false,
)

sealed interface MyAnswerSideEffect {
    data class NavigateToQuestMyAnswerDetail(
        val answerId: Long,
    ) : MyAnswerSideEffect
}
