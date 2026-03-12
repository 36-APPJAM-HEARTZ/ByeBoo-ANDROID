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
        private val routeArgs = savedStateHandle.toRoute<QuestCommonRoute.QuestCommonWriting>()
        private val questId: Long = routeArgs.questId
        private val answerId: Long? = routeArgs.answerId
        private val isEditMode: Boolean = routeArgs.isEditMode
        private val question: String = routeArgs.question

        private val _uiState = MutableStateFlow(QuestCommonState(questId = questId, question = question, isEditMode = isEditMode))
        val uiState: StateFlow<QuestCommonState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestCommonSideEffect>()
        val sideEffect: SharedFlow<QuestCommonSideEffect> = _sideEffect.asSharedFlow()

        init {
            if (isEditMode && answerId != null) {
                loadRecordedContent(answerId = answerId)
            }
        }

    private fun loadRecordedContent(answerId: Long) {
        viewModelScope.launch {
            val cachedAnswer = questCommonRepository.getCachedMyAnswer(answerId = answerId)

            if (cachedAnswer != null) {
                _uiState.update {
                    it.copy(
                        questAnswer = cachedAnswer.content,
                        originalAnswer = cachedAnswer.content,
                    )
                }
            } else {
                questCommonRepository.getCommonQuestAnswerDetail(answerId)
                    .onSuccess { detail ->
                        _uiState.update {
                            it.copy(
                                questAnswer = detail.content,
                                originalAnswer = detail.content,
                                question = detail.question
                            )
                        }
                    }
                    .onFailure {
                        _sideEffect.emit(QuestCommonSideEffect.ShowSnackBar(CustomSnackBarType.ALERT))
                    }
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
            val questAnswer = uiState.value.questAnswer

            viewModelScope.launch {
                _uiState.update { it.copy(showCompleteModal = false) }

                val request = QuestCommonAnswerRequestModel(answer = questAnswer)
                val result =
                    questCommonRepository.uploadQuestCommonAnswer(
                        questId = questId,
                        request = request,
                    )

                result
                    .onSuccess {
                        _sideEffect.emit(QuestCommonSideEffect.NavigateToQuest)
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            QuestCommonSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
                    }
            }
        }

        private fun onSaveEditClicked() {
            val state = uiState.value
            val questAnswer = state.questAnswer
            val currentAnswerId = answerId ?: return

            viewModelScope.launch {
                val request = QuestCommonAnswerEditModel(answer = questAnswer)
                val result =
                    questCommonRepository.patchQuestCommonAnswer(
                        answerId = currentAnswerId,
                        request = request,
                    )

                result
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                questAnswer = questAnswer,
                                isEditMode = false,
                            )
                        }
                        _sideEffect.emit(QuestCommonSideEffect.NavigateToUp)
                    }.onFailure { exception ->
                        _sideEffect.emit(
                            QuestCommonSideEffect.ShowSnackBar(CustomSnackBarType.error(exception)),
                        )
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
