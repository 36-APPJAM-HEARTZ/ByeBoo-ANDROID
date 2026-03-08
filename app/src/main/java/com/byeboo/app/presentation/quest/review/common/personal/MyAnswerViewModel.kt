package com.byeboo.app.presentation.quest.review.common.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.presentation.quest.model.MyAnswerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
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
class MyAnswerViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val questCommonRepository: QuestCommonRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MyAnswerState())
        val uiState: StateFlow<MyAnswerState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<MyAnswerSideEffect>()
        val sideEffect: SharedFlow<MyAnswerSideEffect> = _sideEffect.asSharedFlow()

        init {
            viewModelScope.launch {
                userRepository.getNickname().collect { userName ->
                    _uiState.update { it.copy(userName = userName) }
                }
            }

            viewModelScope.launch {
                questCommonRepository.answersFlow.collect { answerModels ->
                    val answers =
                        answerModels
                            .map {
                                MyAnswerModel(
                                    answerId = it.answerId,
                                    question = it.question,
                                    writtenAt = it.writtenAt,
                                    content = it.content,
                                )
                            }.toPersistentList()
                    _uiState.update { it.copy(answers = answers) }
                }
            }

            loadMyAnswers()
        }

        fun loadMyAnswers() {
            val state = _uiState.value

            if (state.isLoading || !state.hasNext) return

            _uiState.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                questCommonRepository
                    .getQuestCommonMyAnswer()
                    .onSuccess { response ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                hasNext = response.hasNext,
                            )
                        }
                    }.onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                        _sideEffect.emit(MyAnswerSideEffect.ShowSnackBar(snackBarType = CustomSnackBarType.ALERT))
                    }
            }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(MyAnswerSideEffect.NavigateToQuest)
            }
        }

        fun onMyAnswerContentClicked(answerId: Long) {
            viewModelScope.launch {
                _sideEffect.emit(MyAnswerSideEffect.NavigateToQuestMyAnswerDetail(answerId))
            }
        }
    }
