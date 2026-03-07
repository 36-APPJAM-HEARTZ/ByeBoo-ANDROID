package com.byeboo.app.presentation.quest.aianswer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.domain.usecase.quest.GetAiAnswerUseCase
import com.byeboo.app.domain.usecase.quest.PostAiAnswerUseCase
import com.byeboo.app.presentation.quest.navigation.AiAnswerEntryPoint
import com.byeboo.app.presentation.quest.navigation.QuestAiAnswer
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
class QuestAiAnswerViewModel
    @Inject
    constructor(
        private val getAiAnswerUseCase: GetAiAnswerUseCase,
        private val postAiAnswerUseCase: PostAiAnswerUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val args = savedStateHandle.toRoute<QuestAiAnswer>()
        private val questIdArg = args.questId
        private val isExistedAiAnswerArg = args.isExistedAiAnswer
        private val aiAnswerEntryPointArg = args.aiAnswerEntryPoint

        private val _uiState =
            MutableStateFlow(
                QuestAiAnswerState(
                    isExistedAiAnswer = isExistedAiAnswerArg,
                    isLoading = true,
                ),
            )
        val uiState: StateFlow<QuestAiAnswerState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestAiAnswerSideEffect>()
        val sideEffect: SharedFlow<QuestAiAnswerSideEffect> = _sideEffect.asSharedFlow()

        init {
            loadQuestAiAnswer()
        }

        private fun loadQuestAiAnswer() {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                val result =
                    if (isExistedAiAnswerArg) {
                        getAiAnswerUseCase(questId = questIdArg)
                    } else {
                        postAiAnswerUseCase(questId = questIdArg)
                    }

                result
                    .onSuccess { aiAnswer ->
                        _uiState.update { state ->
                            state.copy(
                                questAiAnswer = aiAnswer.aiAnswer,
                                isLoading = false,
                                isFailure = false,
                            )
                        }
                    }.onFailure {
                        _uiState.update { state ->
                            state.copy(
                                questAiAnswer = "",
                                isLoading = false,
                                isFailure = true,
                            )
                        }
                    }
            }
        }

        fun onCloseClicked() {
            viewModelScope.launch {
                if (aiAnswerEntryPointArg == AiAnswerEntryPoint.QUEST) {
                    _sideEffect.emit(QuestAiAnswerSideEffect.NavigateToQuest)
                } else {
                }
            }
        }
    }
