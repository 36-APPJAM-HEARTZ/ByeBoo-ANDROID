package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MyDetailAnswerState(
    val questQuestion: String = "",
    val answer: CommonAnswerModel? = null,
    val comments: ImmutableList<CommonReplyModel> = persistentListOf(),
    val showBottomSheet: Boolean = false,
    val showDeleteModal: Boolean = false,
    val showReplyBottomSheet: Boolean = false,
    val selectedReply: CommonReplyModel? = null,
    val replies: ImmutableList<CommonReplyModel> = persistentListOf(),
    val isLikeLoading: Boolean = false,
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
