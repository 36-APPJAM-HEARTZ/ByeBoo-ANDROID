package com.byeboo.app.presentation.quest.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuestRecordingCompleteViewModel @Inject constructor(
    private val questRecordedDetailRepository: QuestRecordedDetailRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestRecordingCompleteState())
    val uiState: StateFlow<QuestRecordingCompleteState>
        get() = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestRecordingCompleteSideEffect>()
    val sideEffect: SharedFlow<QuestRecordingCompleteSideEffect> = _sideEffect

    fun getQuestRecordedDetail(questId: Long) {
        viewModelScope.launch {
            val result = questRecordedDetailRepository.getQuestRecordedDetail(questId)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        stepNumber = detail.stepNumber,
                        questNumber = detail.questNumber,
                        createdAt = detail.createdAt,
                        question = detail.question,
                        answer = detail.answer,
                        selectedEmotion = LargeTagType.fromKorean(detail.questEmotionState),
                        emotionDescription = detail.emotionDescription
                    )
                }
            }
        }
    }

    fun onCloseClicked() {
        if (uiState.value.questNumber == 30L){
            viewModelScope.launch {
                _sideEffect.emit(QuestRecordingCompleteSideEffect.NavigateToOffboardingCompletedGuide)
            }
        } else {
            viewModelScope.launch {
                _sideEffect.emit(QuestRecordingCompleteSideEffect.NavigateToQuest)
            }
        }
    }

    fun setQuestId(questId: Long) {
        _uiState.update {
            it.copy(questId = questId)
        }
    }
}
