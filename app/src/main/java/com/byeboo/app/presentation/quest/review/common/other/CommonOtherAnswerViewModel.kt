package com.byeboo.app.presentation.quest.review.common.other

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.presentation.quest.component.type.OtherPostOption
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import com.byeboo.app.presentation.quest.navigation.QuestCommonAnswer
import com.byeboo.app.presentation.quest.util.QuestUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
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
    ) : ViewModel() {
        private val routeArgs = savedStateHandle.toRoute<QuestCommonAnswer>()
        private val answerId: Long = routeArgs.answerId

        private val _uiState = MutableStateFlow(CommonAnswerState())
        val uiState: StateFlow<CommonAnswerState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<CommonAnswerSideEffect>()
        val sideEffect: SharedFlow<CommonAnswerSideEffect> = _sideEffect.asSharedFlow()

        init {
            loadCommonAnswer()
        }

        private fun loadCommonAnswer() {
            viewModelScope.launch {
                commonQuestRepository
                    .getCommonQuestAnswerDetail(answerId)
                    .onSuccess { domainModel ->

                        _uiState.update { state ->
                            state.copy(
                                questQuestion = domainModel.question,
                                createdAt = mapper.formatDetailDate(domainModel.writtenAt),
                                writerId = domainModel.writerId,
                                answer =
                                    CommonAnswerModel(
                                        answerId = answerId,
                                        writer = domainModel.writer,
                                        writerId = domainModel.writerId,
                                        profileIconRes = mapper.mapToIconRes(domainModel.profileIcon),
                                        displayTime = mapper.formatDetailDate(domainModel.writtenAt),
                                        content = domainModel.content,
                                    ),
                            )
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
                _sideEffect.emit(CommonAnswerSideEffect.NavigateToQuest)
            }
        }

        fun onClickMoreOptions() {
            _uiState.update { it.copy(showBottomSheet = true) }
        }

        fun onDismissBottomSheet() {
            _uiState.update { it.copy(showBottomSheet = false) }
        }

        fun onOptionClicked(option: OtherPostOption) {
            onDismissBottomSheet()
            val currentWriterId = uiState.value.writerId
            viewModelScope.launch {
                when (option) {
                    OtherPostOption.BLOCK -> {
                        commonQuestRepository
                            .updateBlockedUser(currentWriterId)
                            .onSuccess {
                                _sideEffect.emit(CommonAnswerSideEffect.NavigateToQuest)
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(
                                        snackBarType = CustomSnackBarType.SUCCESS("차단이 완료되었어요. 이에 해당 사용자의 글이 노출되지 않아요."),
                                    ),
                                )
                            }.onFailure { exception ->
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                                )
                            }
                    }

                    OtherPostOption.REPORT -> {
                        commonQuestRepository
                            .reportCommonQuest(answerId)
                            .onSuccess {
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(
                                        snackBarType = CustomSnackBarType.SUCCESS("신고가 접수되었어요. 처리 결과는 알림을 통해 알려드려요."),
                                    ),
                                )
                            }.onFailure { exception ->
                                _sideEffect.emit(
                                    CommonAnswerSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                                )
                            }
                    }
                }
            }
        }
    }
