package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import com.byeboo.app.presentation.quest.navigation.QuestMyAnswersDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MyDetailAnswerViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val questCommonRepository: QuestCommonRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MyDetailAnswerState())
        val uiState: StateFlow<MyDetailAnswerState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<MyDetailAnswerSideEffect>()
        val sideEffect: SharedFlow<MyDetailAnswerSideEffect> = _sideEffect.asSharedFlow()

        private val answerId: Long = savedStateHandle.toRoute<QuestMyAnswersDetail>().answerId

        init {
            loadMyDetailAnswer()
        }

        fun loadMyDetailAnswer() {
            viewModelScope.launch {
                val cached = questCommonRepository.getCachedMyAnswer(answerId)
                if (cached != null) {
                    _uiState.update { state ->
                        state.copy(
                            answer =
                                state.answer.copy(
                                    answerId = cached.answerId,
                                    question = cached.question,
                                    writtenAt = cached.writtenAt,
                                    content = cached.content,
                                ),
                        )
                    }
                } else {
                    questCommonRepository
                        .getCommonQuestAnswerDetail(answerId)
                        .onSuccess { detail ->
                            _uiState.update { state ->
                                state.copy(
                                    answer =
                                        state.answer.copy(
                                            answerId = answerId,
                                            question = detail.question,
                                            writtenAt = detail.writtenAt.toString(),
                                            content = detail.content,
                                        ),
                                )
                            }
                        }.onFailure {
                            _sideEffect.emit(MyDetailAnswerSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
                        }
                }
            }
        }

        fun onClickMoreOptions() {
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
                val question = _uiState.value.answer.question
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
    }
