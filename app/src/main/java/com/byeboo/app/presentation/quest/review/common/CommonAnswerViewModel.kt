package com.byeboo.app.presentation.quest.review.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.ReportType
import com.byeboo.app.core.util.TimeFormatter
import com.byeboo.app.domain.model.quest.CommonQuestCommentEditModel
import com.byeboo.app.domain.model.quest.ReportCommentQuestModel
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.presentation.quest.component.type.MoreOptionTarget
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import com.byeboo.app.presentation.quest.component.type.OtherPostOption
import com.byeboo.app.presentation.quest.component.type.PostOption
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import com.byeboo.app.presentation.quest.navigation.QuestCommonAnswer
import com.byeboo.app.presentation.quest.util.QuestUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonOtherAnswerViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val commonQuestRepository: QuestCommonRepository,
        private val mapper: QuestUiModelMapper,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val routeArgs = savedStateHandle.toRoute<QuestCommonAnswer>()
        private val answerId: Long = routeArgs.answerId

        private val _uiState = MutableStateFlow(CommonAnswerState())
        val uiState: StateFlow<CommonAnswerState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<CommonAnswerSideEffect>()
        val sideEffect: SharedFlow<CommonAnswerSideEffect> = _sideEffect.asSharedFlow()

        private val _replySubmissionSuccess = MutableSharedFlow<Unit>()
        val replySubmissionSuccess: SharedFlow<Unit> = _replySubmissionSuccess.asSharedFlow()

        private val _commentSubmissionSuccess = MutableSharedFlow<Unit>()
        val commentSubmissionSuccess: SharedFlow<Unit> = _commentSubmissionSuccess.asSharedFlow()

        init {
            loadCurrentUserId()
            loadCommonAnswer()
            observeRefreshEvent()
        }

        private fun observeRefreshEvent() {
            viewModelScope.launch {
                commonQuestRepository.refreshEvent.collect {
                    loadCommonAnswer()
                }
            }
        }

        private fun loadCurrentUserId() {
            viewModelScope.launch {
                userRepository.getUserId()?.let { userId ->
                    _uiState.update { it.copy(currentUserId = userId) }
                }
            }
        }

        private fun loadCommonAnswer(notifyCommentSubmissionSuccess: Boolean = false) {
            viewModelScope.launch {
                commonQuestRepository
                    .getCommonQuestAnswerDetail(answerId)
                    .onSuccess { domainModel ->
                        val answer = domainModel.answer
                        _uiState.update { state ->
                            state.copy(
                                questQuestion = domainModel.question,
                                createdAt = mapper.formatDetailDate(answer.writtenAt),
                                answer =
                                    CommonAnswerModel(
                                        heartCount = answer.heartCount,
                                        commentCount = answer.commentCount,
                                        isLiked = answer.isLiked,
                                        answerId = answerId,
                                        writer = answer.writer,
                                        writerId = answer.writerId,
                                        profileIconRes = mapper.mapToIconRes(answer.profileIcon),
                                        displayTime = mapper.formatDetailDate(answer.writtenAt),
                                        content = answer.content,
                                    ),
                                comments =
                                    domainModel.comments
                                        .map { comment ->
                                            CommonReplyModel(
                                                replyId = comment.commentId,
                                                writerId = comment.writerId,
                                                writer = comment.writer,
                                                profileIconRes = mapper.mapToIconRes(comment.profileIcon),
                                                displayTime = TimeFormatter.formatWrittenTime(comment.writtenAt),
                                                content = comment.content,
                                                replyCount = comment.replyCount.toInt(),
                                            )
                                        }.toImmutableList(),
                            )
                        }

                        if (notifyCommentSubmissionSuccess) {
                            _commentSubmissionSuccess.emit(Unit)
                        }
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            CommonAnswerSideEffect.ShowSnackBar(
                                CustomSnackBarType.error(exception),
                            ),
                        )
                    }
            }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(CommonAnswerSideEffect.NavigateUp)
            }
        }

        fun onClickMoreOptions(target: MoreOptionTarget) {
            _uiState.update { it.copy(selectedTarget = target) }
        }

        fun onDismissBottomSheet() {
            _uiState.update { it.copy(selectedTarget = null) }
        }

        fun onHeartClicked() {
            if (_uiState.value.isLikeLoading) return
            val previousAnswer = _uiState.value.answer ?: return

            _uiState.update {
                it.copy(
                    answer = mapper.toggleLike(previousAnswer),
                    isLikeLoading = true,
                )
            }

            viewModelScope.launch {
                commonQuestRepository
                    .updateAnswerLike(answerId)
                    .onSuccess { result ->
                        _uiState.update { state ->
                            state.copy(
                                isLikeLoading = false,
                                answer =
                                    state.answer?.copy(
                                        isLiked = result.isLiked,
                                        heartCount = result.heartCount,
                                    ),
                            )
                        }
                    }.onFailure { exception ->
                        _uiState.update { it.copy(answer = previousAnswer, isLikeLoading = false) }
                        _sideEffect.emit(
                            CommonAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        fun onCommentClicked(comment: CommonReplyModel) {
            _uiState.update {
                it.copy(
                    showReplyBottomSheet = true,
                    selectedComment = comment,
                    selectedReplies = persistentListOf(),
                )
            }
            loadCommentReplies(commentId = comment.replyId)
        }

        private fun loadCommentReplies(commentId: Long) {
            viewModelScope.launch {
                commonQuestRepository
                    .getCommentReplies(commentId)
                    .onSuccess { result ->
                        _uiState.update {
                            val updatedComment =
                                CommonReplyModel(
                                    replyId = result.comment.commentId,
                                    writerId = result.comment.writerId,
                                    writer = result.comment.writer,
                                    profileIconRes = mapper.mapToIconRes(result.comment.profileIcon),
                                    displayTime = TimeFormatter.formatWrittenTime(result.comment.writtenAt),
                                    content = result.comment.content,
                                    replyCount = result.totalCount,
                                )

                            it.copy(
                                selectedComment = updatedComment,
                                selectedReplies =
                                    result.replies
                                        .map { reply ->
                                            CommonReplyModel(
                                                replyId = reply.replyId,
                                                writerId = reply.writerId,
                                                writer = reply.writer,
                                                profileIconRes = mapper.mapToIconRes(reply.profileIcon),
                                                displayTime = TimeFormatter.formatWrittenTime(reply.writtenAt),
                                                content = reply.content,
                                                replyCount = 0,
                                            )
                                        }.toImmutableList(),
                            )
                        }
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            CommonAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        fun onDismissReplyBottomSheet() {
            _uiState.update {
                it.copy(
                    showReplyBottomSheet = false,
                    selectedComment = null,
                )
            }
        }

        fun onCommentComplete(content: String) {
            viewModelScope.launch {
                commonQuestRepository
                    .uploadComment(
                        content = content,
                        targetId = answerId,
                    ).onSuccess {
                        loadCommonAnswer(notifyCommentSubmissionSuccess = true)
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            CommonAnswerSideEffect.ShowSnackBar(
                                CustomSnackBarType.error(exception),
                            ),
                        )
                    }
            }
        }

        fun onReplyComplete(content: String) {
            if (_uiState.value.isReplySubmitting) return
            val commentId = uiState.value.selectedComment?.replyId ?: return
            _uiState.update { it.copy(isReplySubmitting = true) }

            viewModelScope.launch {
                commonQuestRepository
                    .uploadCommentReply(
                        commentId = commentId,
                        content = content,
                    ).onSuccess {
                        _uiState.update { it.copy(isReplySubmitting = false) }
                        _replySubmissionSuccess.emit(Unit)
                        loadCommentReplies(commentId)
                        updateCommentReplyCount(commentId)
                    }.onFailure { exception ->
                        _uiState.update { it.copy(isReplySubmitting = false) }
                        _sideEffect.emit(
                            CommonAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        fun onEditCommentComplete(content: String) {
            val editingComment = uiState.value.editingComment ?: return
            val isReplySheetSubmission = uiState.value.showReplyBottomSheet
            if (isReplySheetSubmission) {
                if (_uiState.value.isReplySubmitting) return
                _uiState.update { it.copy(isReplySubmitting = true) }
            }

            viewModelScope.launch {
                commonQuestRepository
                    .updateCommonQuestComment(
                        commentId = editingComment.target.id,
                        request = CommonQuestCommentEditModel(content = content),
                    ).onSuccess {
                        _uiState.update {
                            it.copy(
                                editingComment = null,
                                isReplySubmitting =
                                    if (isReplySheetSubmission) {
                                        false
                                    } else {
                                        it.isReplySubmitting
                                    },
                            )
                        }
                        if (isReplySheetSubmission) {
                            _replySubmissionSuccess.emit(Unit)
                        }

                        when (editingComment.target) {
                            is MoreOptionTarget.Comment -> {
                                if (_uiState.value.showReplyBottomSheet) {
                                    loadCommentReplies(editingComment.target.id)
                                }
                            }

                            is MoreOptionTarget.Reply -> {
                                uiState.value.selectedComment?.replyId?.let { commentId ->
                                    loadCommentReplies(commentId)
                                }
                            }

                            is MoreOptionTarget.Answer -> Unit
                        }
                    }.onFailure { exception ->
                        if (isReplySheetSubmission) {
                            _uiState.update { it.copy(isReplySubmitting = false) }
                        }
                        _sideEffect.emit(
                            CommonAnswerSideEffect.ShowSnackBar(
                                CustomSnackBarType.error(exception),
                            ),
                        )
                    }
            }
        }

        private fun updateCommentReplyCount(commentId: Long) {
            _uiState.update { state ->
                state.copy(
                    comments =
                        state.comments
                            .map { comment ->
                                if (comment.replyId == commentId) {
                                    comment.copy(replyCount = comment.replyCount + 1)
                                } else {
                                    comment
                                }
                            }.toImmutableList(),
                    selectedComment =
                        state.selectedComment?.let {
                            if (it.replyId == commentId) it.copy(replyCount = it.replyCount + 1) else it
                        },
                )
            }
        }

        fun onOptionClicked(option: PostOption) {
            val target = uiState.value.selectedTarget ?: return

            when (option) {
                is MyPostOption -> onMyPostOptionClicked(option = option, target = target)
                is OtherPostOption -> onOtherPostOptionClicked(option = option, target = target)
            }
        }

        fun onMyPostOptionClicked(
            option: MyPostOption,
            target: MoreOptionTarget,
        ) {
            viewModelScope.launch {
                when (option) {
                    MyPostOption.EDIT -> onMyPostEditClicked(target)

                    MyPostOption.DELETE -> {
                        _uiState.update {
                            it.copy(
                                showDeleteModal = true,
                                deleteTarget = target,
                                selectedTarget = null,
                            )
                        }
                    }
                }
            }
        }

        private fun onMyPostEditClicked(target: MoreOptionTarget) {
            onDismissBottomSheet()

            viewModelScope.launch {
                when (target) {
                    is MoreOptionTarget.Answer -> {
                        _sideEffect.emit(
                            CommonAnswerSideEffect.NavigateToQuestCommonEdit(
                                answerId = answerId,
                                question = uiState.value.questQuestion,
                                isEditMode = true,
                            ),
                        )
                    }

                    is MoreOptionTarget.Comment,
                    is MoreOptionTarget.Reply,
                    -> {
                        val content = findCommentContent(target) ?: return@launch

                        _uiState.update {
                            it.copy(
                                editingComment =
                                    EditingCommentState(
                                        target = target,
                                        content = content,
                                    ),
                            )
                        }
                    }
                }
            }
        }

        private fun findCommentContent(target: MoreOptionTarget): String? =
            when (target) {
                is MoreOptionTarget.Comment ->
                    uiState.value.comments
                        .firstOrNull { it.replyId == target.id }
                        ?.content
                        ?: uiState.value.selectedComment
                            ?.takeIf { it.replyId == target.id }
                            ?.content

                is MoreOptionTarget.Reply ->
                    uiState.value.selectedReplies
                        .firstOrNull { it.replyId == target.id }
                        ?.content

                is MoreOptionTarget.Answer -> null
            }

        fun onDismissDeleteModal() {
            _uiState.update {
                it.copy(
                    showDeleteModal = false,
                    deleteTarget = null,
                )
            }
        }

        fun onQuestDeleteClicked() {
            val target = uiState.value.deleteTarget ?: return

            viewModelScope.launch {
                when (target) {
                    is MoreOptionTarget.Answer -> {
                        commonQuestRepository
                            .deleteQuestCommonAnswer(answerId = answerId)
                            .onSuccess {
                                _uiState.update {
                                    it.copy(
                                        showDeleteModal = false,
                                        deleteTarget = null,
                                    )
                                }
                                _sideEffect.emit(CommonAnswerSideEffect.NavigateUp)
                            }.onFailure {
                                _sideEffect.emit(CommonAnswerSideEffect.ShowSnackBar(snackBarType = CustomSnackBarType.ALERT))
                            }
                    }

                    is MoreOptionTarget.Comment,
                    is MoreOptionTarget.Reply,
                    -> {
                        commonQuestRepository
                            .deleteCommonQuestComment(commentId = target.id)
                            .onSuccess {
                                _uiState.update {
                                    it.copy(
                                        showDeleteModal = false,
                                        deleteTarget = null,
                                    )
                                }

                                when (target) {
                                    is MoreOptionTarget.Comment -> {
                                        loadCommonAnswer()

                                        if (uiState.value.selectedComment?.replyId == target.id) {
                                            _uiState.update {
                                                it.copy(
                                                    showReplyBottomSheet = false,
                                                    selectedComment = null,
                                                    selectedReplies = persistentListOf(),
                                                )
                                            }
                                        }
                                    }

                                    is MoreOptionTarget.Reply -> {
                                        uiState.value.selectedComment?.replyId?.let { commentId ->
                                            loadCommentReplies(commentId)
                                        }
                                        loadCommonAnswer()
                                    }

                                    else -> Unit
                                }
                                loadCommonAnswer()
                            }.onFailure { exception ->
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(
                                        CustomSnackBarType.error(
                                            exception,
                                        ),
                                    ),
                                )
                            }
                    }
                }
            }
        }

        fun onOtherPostOptionClicked(
            option: OtherPostOption,
            target: MoreOptionTarget,
        ) {
            onDismissBottomSheet()

            viewModelScope.launch {
                when (option) {
                    OtherPostOption.BLOCK -> {
                        commonQuestRepository
                            .updateBlockedUser(target.writerId)
                            .onSuccess {
                                _sideEffect.emit(CommonAnswerSideEffect.NavigateUp)
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(
                                        snackBarType = CustomSnackBarType.SUCCESS("차단이 완료되었어요. 이에 해당 사용자의 글이 노출되지 않아요."),
                                    ),
                                )
                            }.onFailure { exception ->
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(
                                        CustomSnackBarType.error(
                                            exception,
                                        ),
                                    ),
                                )
                            }
                    }

                    OtherPostOption.REPORT -> {
                        val request =
                            ReportCommentQuestModel(
                                reportType = target.toReportType(),
                                targetId = target.id,
                            )
                        commonQuestRepository
                            .reportCommonQuest(request)
                            .onSuccess {
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(
                                        snackBarType = CustomSnackBarType.SUCCESS("신고가 접수되었어요. 처리 결과는 알림을 통해 알려드려요."),
                                    ),
                                )
                            }.onFailure { exception ->
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(
                                        CustomSnackBarType.error(
                                            exception,
                                        ),
                                    ),
                                )
                            }
                    }
                }
            }
        }
    }

private fun MoreOptionTarget.toReportType(): ReportType =
    when (this) {
        is MoreOptionTarget.Answer -> ReportType.COMMON_QUEST
        is MoreOptionTarget.Comment -> ReportType.COMMENT
        is MoreOptionTarget.Reply -> ReportType.COMMENT
    }
