package com.byeboo.app.presentation.quest.review.common.other

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class CommonAnswerState(
    val createdAt: String = LocalDate.now().toString(),
    val questQuestion: String = "",
    val answer: CommonAnswerModel? = null,
    val comments: ImmutableList<CommonReplyModel> = persistentListOf(),
    val showBottomSheet: Boolean = false,
    val showReplyBottomSheet: Boolean = false,
    val selectedReply: CommonReplyModel? = null,
    val replies: ImmutableList<CommonReplyModel> = persistentListOf(),
)

sealed interface CommonAnswerSideEffect {
    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : CommonAnswerSideEffect

    data object NavigateToQuest : CommonAnswerSideEffect
}