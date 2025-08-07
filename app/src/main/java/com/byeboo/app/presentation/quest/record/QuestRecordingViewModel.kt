package com.byeboo.app.presentation.quest.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.model.quest.QuestRecording
import com.byeboo.app.domain.repository.quest.QuestDetailRecordingRepository
import com.byeboo.app.domain.repository.quest.QuestRecordingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestRecordingViewModel @Inject constructor(
    val questDetailRecordingRepository: QuestDetailRecordingRepository,
    val questRecordingRepository: QuestRecordingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestRecordingState())
    val uiState: StateFlow<QuestRecordingState>
        get() = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestRecordingSideEffect>()
    val sideEffect: SharedFlow<QuestRecordingSideEffect>
        get() = _sideEffect

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _isEmotionSelected = MutableStateFlow(false)
    val isEmotionSelected: StateFlow<Boolean> = _isEmotionSelected.asStateFlow()

    fun setQuestId(questId: Long) {
        _uiState.update {
            it.copy(questId = questId)
        }
    }

    fun getQuestDetailInfo(questId: Long) {
        viewModelScope.launch {
            val result = questDetailRecordingRepository.getQuestRecordingDetail(questId)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        step = detail.step,
                        stepNumber = detail.stepNumber,
                        questNumber = detail.questNumber,
                        questQuestion = detail.question
                    )
                }
            }
        }
    }

    fun postQuestRecording() {
        val questId = uiState.value.questId
        val answer = uiState.value.questAnswer
        val emotion = uiState.value.selectedEmotion.title

        viewModelScope.launch {
            val request = QuestRecording(
                answer = answer,
                questEmotionState = emotion
            )
            val result = questRecordingRepository.postRecording(questId, request)

            if (result.isSuccess) {
                _showBottomSheet.value = false
                _sideEffect.emit(QuestRecordingSideEffect.NavigateToQuestRecordingComplete(questId))
            }
        }
    }

    fun updateContent(isFocused: Boolean, questAnswer: String) {
        val contentState = QuestContentLengthValidator.validate(isFocused, questAnswer)
        _uiState.update {
            it.copy(
                questAnswer = questAnswer,
                contentsState = contentState
            )
        }
    }

    fun onBackClick() {
        _uiState.update { it.copy(showQuitModal = true) }
    }

    fun onDismissModal() {
        _uiState.update { it.copy(showQuitModal = false) }
    }

    fun onQuitClick() {
        viewModelScope.launch {
            _sideEffect.emit(QuestRecordingSideEffect.NavigateToQuest)
        }
    }

    fun onTipClick() {
        val questId = uiState.value.questId
        viewModelScope.launch {
            _sideEffect.emit(
                QuestRecordingSideEffect.NavigateToQuestTip(
                    questId,
                    QuestType.RECORDING
                )
            )
        }
    }

    fun openBottomSheet() {
        _showBottomSheet.value = true
    }

    fun closeBottomSheet() {
        _showBottomSheet.value = false
    }

    fun isEmotionSelected(isSelected: Boolean) {
        _isEmotionSelected.value = isSelected
    }

    fun updateSelectedEmotion(emotion: LargeTagType) {
        _uiState.value = _uiState.value.copy(selectedEmotion = emotion)
    }
}
