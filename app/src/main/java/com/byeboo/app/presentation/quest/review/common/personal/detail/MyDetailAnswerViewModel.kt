package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.util.DateUtil
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import com.byeboo.app.presentation.quest.navigation.QuestMyAnswersDetail
import com.byeboo.app.presentation.quest.util.QuestUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MyDetailAnswerViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val mapper: QuestUiModelMapper,
        private val questCommonRepository: QuestCommonRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MyDetailAnswerState())
        val uiState: StateFlow<MyDetailAnswerState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<MyDetailAnswerSideEffect>()
        val sideEffect: SharedFlow<MyDetailAnswerSideEffect> = _sideEffect.asSharedFlow()

        private val answerId: Long = savedStateHandle.toRoute<QuestMyAnswersDetail>().answerId

        init {
            loadMyDetailAnswer()
            observeRefreshEvent()
        }

        private fun loadMyDetailAnswer() {
            observeAnswerUpdates()
            loadAnswerDetail()
        }

        private fun observeAnswerUpdates() {
            questCommonRepository.answersFlow
                .mapNotNull { list -> list.find { it.answerId == answerId } }
                .onEach { updated ->
                    _uiState.update { state ->
                        state.copy(
                            questQuestion = updated.question,
                            answer =
                                state.answer?.copy(
                                    content = updated.content,
                                    displayTime = DateUtil.formatToDotDate(updated.writtenAt),
                                ),
                        )
                    }
                }.launchIn(viewModelScope)
        }

        private fun loadAnswerDetail() {
            viewModelScope.launch {
                questCommonRepository
                    .getCommonQuestAnswerDetail(answerId)
                    .onSuccess { detail ->
                        val answer = detail.answer

                        _uiState.update {
                            it.copy(
                                questQuestion = detail.question,
                                answer =
                                    CommonAnswerModel(
                                        heartCount = answer.heartCount,
                                        commentCount = answer.commentCount,
                                        isLiked = answer.isLiked,
                                        answerId = answerId,
                                        writerId = answer.writerId,
                                        writer = answer.writer,
                                        profileIconRes = mapper.mapToIconRes(answer.profileIcon),
                                        displayTime = mapper.formatDetailDate(answer.writtenAt),
                                        content = answer.content,
                                    ),
                                comments =
                                    detail.comments
                                        .map { comment ->
                                            CommonReplyModel(
                                                replyId = comment.commentId,
                                                writerId = comment.writerId,
                                                writer = comment.writer,
                                                profileIconRes = mapper.mapToIconRes(comment.profileIcon),
                                                displayTime = mapper.formatWrittenTime(comment.writtenAt),
                                                content = comment.content,
                                                replyCount = comment.replyCount.toInt(),
                                            )
                                        }.toImmutableList(),
                            )
                        }
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            MyDetailAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        fun onClickMoreOptions() {
            if (_uiState.value.answer == null) return

            _uiState.update { it.copy(showBottomSheet = true) }
        }

        fun onDismissBottomSheet() {
            _uiState.update { it.copy(showBottomSheet = false) }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(MyDetailAnswerSideEffect.NavigateUp)
            }
        }

        fun onOptionClicked(option: MyPostOption) {
            onDismissBottomSheet()

            viewModelScope.launch {
                val question = _uiState.value.questQuestion
                when (option) {
                    MyPostOption.EDIT -> {
                        _sideEffect.emit(
                            MyDetailAnswerSideEffect.NavigateToQuestCommonEdit(
                                answerId = answerId,
                                question = question,
                                isEditMode = true,
                            ),
                        )
                    }
                    MyPostOption.DELETE -> {
                        _uiState.update { it.copy(showDeleteModal = true) }
                    }
                }
            }
        }

        fun onDismissDeleteModal() {
            _uiState.update { it.copy(showDeleteModal = false) }
        }

        fun onQuestDeleteClicked() {
            viewModelScope.launch {
                questCommonRepository
                    .deleteQuestCommonAnswer(answerId = answerId)
                    .onSuccess {
                        _uiState.update { it.copy(showDeleteModal = false) }
                        _sideEffect.emit(MyDetailAnswerSideEffect.NavigateUp)
                    }.onFailure {
                        _sideEffect.emit(MyDetailAnswerSideEffect.ShowSnackBar(snackBarType = CustomSnackBarType.ALERT))
                    }
            }
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
                questCommonRepository
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
                            MyDetailAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        fun onCommentClick(reply: CommonReplyModel) {
            _uiState.update {
                it.copy(
                    showReplyBottomSheet = true,
                    selectedReply = reply,
                )
            }
            loadCommentReplies(reply.replyId)
        }

        private fun loadCommentReplies(commentId: Long) {
            viewModelScope.launch {
                questCommonRepository
                    .getCommentReplies(commentId)
                    .onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                replies =
                                    result.replies
                                        .map { reply ->
                                            CommonReplyModel(
                                                replyId = reply.commentId,
                                                writerId = reply.writerId,
                                                writer = reply.writer,
                                                profileIconRes = mapper.mapToIconRes(reply.profileIcon),
                                                displayTime = mapper.formatWrittenTime(reply.writtenAt),
                                                content = reply.content,
                                                replyCount = 0,
                                            )
                                        }.toImmutableList(),
                            )
                        }
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            MyDetailAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        fun onDismissReplyBottomSheet() {
            _uiState.update {
                it.copy(
                    showReplyBottomSheet = false,
                    selectedReply = null,
                )
            }
        }

        fun onCompleteComment(content: String) {
            viewModelScope.launch {
                questCommonRepository
                    .uploadComment(
                        content = content,
                        targetId = answerId,
                    ).onSuccess {
                        loadAnswerDetail()
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            MyDetailAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        fun onCompleteReply(content: String) {
            val commentId = uiState.value.selectedReply?.replyId ?: return
            viewModelScope.launch {
                questCommonRepository
                    .uploadCommentReply(
                        commentId = commentId,
                        content = content,
                    ).onSuccess {
                        loadCommentReplies(commentId)
                        updateCommentReplyCount(commentId)
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            MyDetailAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
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
                    selectedReply =
                        state.selectedReply?.let {
                            if (it.replyId == commentId) it.copy(replyCount = it.replyCount + 1) else it
                        },
                )
            }
        }

        private fun observeRefreshEvent() {
            viewModelScope.launch {
                questCommonRepository.refreshEvent.collect {
                    val cached = questCommonRepository.getCachedMyAnswer(answerId)
                    if (cached == null) {
                        loadAnswerDetail()
                    }
                }
            }
        }
    }
