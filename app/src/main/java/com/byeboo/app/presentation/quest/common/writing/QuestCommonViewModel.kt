package com.byeboo.app.presentation.quest.common.writing

import androidx.lifecycle.ViewModel
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QuestCommonViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestCommonState())
    val uiState: StateFlow<QuestCommonState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestCommonSideEffect>()
    val sideEffect: SharedFlow<QuestCommonSideEffect> = _sideEffect.asSharedFlow()

    fun onBackClicked() {

    }

    fun onCompleteClicked() {

    }

    fun updateContent(
        isFocused: Boolean,
        questAnswer: String
    ) {
        val contentState = QuestContentLengthValidator.validate(isFocused, questAnswer)
        _uiState.update { prev ->
            val hasAnswerChanged = questAnswer != prev.originalAnswer

            val next =
                prev.copy(
                    questAnswer = questAnswer,
                    contentsState = contentState,
                    hasAnswerChanged = hasAnswerChanged,
                )
            val isButtonEnabled =
                completeButtonEnabled(
                    state = next,
                )

            next.copy(
                isCompleteButtonEnabled = isButtonEnabled,
            )
        }
    }

    private fun completeButtonEnabled(state: QuestCommonState): Boolean {
        val isValid = QuestContentLengthValidator.validButton(state.questAnswer)

        return if (state.isEditMode) {
            isValid && state.hasAnswerChanged
        } else {
            isValid
        }
    }
}