package com.byeboo.app.presentation.quest.common.writing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.model.quest.QuestCommonAnswerEditModel
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
import com.byeboo.app.presentation.quest.common.navigation.QuestCommonRoute
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
class QuestCommonWritingViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val questCommonRepository: QuestCommonRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QuestCommonState())
        val uiState: StateFlow<QuestCommonState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestCommonSideEffect>()
        val sideEffect: SharedFlow<QuestCommonSideEffect> = _sideEffect.asSharedFlow()

        private val routeArgs = savedStateHandle.toRoute<QuestCommonRoute.QuestCommonWriting>()
        private val questId = routeArgs.questId
        private val answerId: Long? = routeArgs.answerId
        private val isEditMode = routeArgs.isEditMode

        init {
            if (isEditMode && answerId != null) {
                loadRecordedContent(answerId = answerId)
            }
        }

    private fun loadRecordedContent(answerId: Long) {
        val cachedAnswer = questCommonRepository.getCachedMyAnswer(answerId = answerId)

        if (cachedAnswer != null) {
            _uiState.update {
                it.copy(
                    questAnswer = cachedAnswer.content
                )
            }
        }
    }

        fun onBackClicked() {
            _uiState.update { it.copy(showQuitModal = true) }
        }

        fun onCompleteClicked() {
            if (isEditMode) {
                onSaveEditClicked()
            } else {
                _uiState.update { it.copy(showCompleteModal = true) }
            }
        }

        fun updateContent(
            isFocused: Boolean,
            questAnswer: String,
        ) {
            val contentState = QuestContentLengthValidator.validate(isFocused, questAnswer)
            _uiState.update { prev ->
                prev.copy(
                    questAnswer = questAnswer,
                    contentsState = contentState,
                )
            }
        }

        fun onSaveClicked() {
            val state = uiState.value
            val questId = state.questId
            val questAnswer = state.questAnswer

            viewModelScope.launch {
                val request =
                    QuestCommonAnswerRequestModel(
                        answer = questAnswer
                    )

                // Todo : 머지 후, questId = questId 로 수정 예정
                val result = questCommonRepository.uploadQuestCommonAnswer(questId = 72, request = request)

                result.onSuccess {
                    _uiState.update { it.copy(showCompleteModal = false) }
                    _sideEffect.emit(QuestCommonSideEffect.NavigateToQuest)

                }.onFailure {
                    _sideEffect.emit(QuestCommonSideEffect.ShowSnackBar(snackBarType = CustomSnackBarType.ALERT))
                }
            }


        }

        private fun onSaveEditClicked() {
            val state = uiState.value
            val questAnswer = state.questAnswer
            val answerId = answerId ?: return

            viewModelScope.launch {
                val request = QuestCommonAnswerEditModel(answer = questAnswer)
                val result = questCommonRepository.patchQuestCommonAnswer(
                    answerId = answerId,
                    request = request
                )

                result.onSuccess {
                    _uiState.update { it.copy(
                        questAnswer = questAnswer,
                        isEditMode = false)
                    }
                    _sideEffect.emit(QuestCommonSideEffect.NavigateToUp)
                }.onFailure {
                    _sideEffect.emit(QuestCommonSideEffect.ShowSnackBar(snackBarType = CustomSnackBarType.ALERT))
                }
            }
        }

        fun onDismissQuitModal() {
            _uiState.update { it.copy(showQuitModal = false) }
        }

        fun onDismissCompleteModal() {
            _uiState.update { it.copy(showCompleteModal = false) }
        }

    fun onQuitClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestCommonSideEffect.NavigateToUp)
        }
    }
    }
