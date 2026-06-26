package com.byeboo.app.presentation.quest.review.common

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.component.type.MoreOptionTarget
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class CommonAnswerState(
    val createdAt: String = LocalDate.now().toString(),
    val currentUserId: Long = -1,
    val selectedTarget: MoreOptionTarget? = null,
    val deleteTarget: MoreOptionTarget? = null,
    val editingComment: EditingCommentState? = null,
    val questQuestion: String = "",
    val answer: CommonAnswerModel? = null,
    val comments: ImmutableList<CommonReplyModel> = persistentListOf(),
    val selectedComment: CommonReplyModel? = null,
    val selectedReplies: ImmutableList<CommonReplyModel> = persistentListOf(),
    val showReplyBottomSheet: Boolean = false,
    val showDeleteModal: Boolean = false,
    val isLikeLoading: Boolean = false,
) {
    val showBottomSheet: Boolean
        get() = selectedTarget != null
    val isMine: Boolean
        get() = selectedTarget?.writerId == currentUserId
}

data class EditingCommentState(
    val target: MoreOptionTarget,
    val content: String,
)

sealed interface CommonAnswerSideEffect {
    data object NavigateUp : CommonAnswerSideEffect

    data object NavigateToQuestMyAnswers : CommonAnswerSideEffect

    data class NavigateToQuestCommonEdit(
        val answerId: Long,
        val question: String,
        val isEditMode: Boolean,
    ) : CommonAnswerSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : CommonAnswerSideEffect
}
