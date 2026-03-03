package com.byeboo.app.presentation.quest.common.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.model.quest.QuestCommonAnswerRequestModel
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.repository.quest.QuestCommonRepository
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
        private val questCommonRepository: QuestCommonRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QuestCommonState())
        val uiState: StateFlow<QuestCommonState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestCommonSideEffect>()
        val sideEffect: SharedFlow<QuestCommonSideEffect> = _sideEffect.asSharedFlow()

        fun onBackClicked() {
            _uiState.update { it.copy(showQuitModal = true) }
        }

        fun onCompleteClicked() {
            if (uiState.value.isEditMode) {
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

                val result = questCommonRepository.uploadQuestCommonAnswer(questId = 69, request = request)

                result.onSuccess {
                    // 나가지기
                    // 공통여정 화면에서 카드 띄우기
                    _uiState.update { it.copy(showCompleteModal = false) }
                    _sideEffect.emit(QuestCommonSideEffect.NavigateToQuest)

                }
            }


        }

        private fun onSaveEditClicked() {

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
