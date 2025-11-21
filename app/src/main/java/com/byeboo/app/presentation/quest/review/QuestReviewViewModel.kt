package com.byeboo.app.presentation.quest.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
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
class QuestReviewViewModel @Inject constructor(
    val questRecordedDetailRepository: QuestRecordedDetailRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestReviewState())
    val uiState: StateFlow<QuestReviewState>
        get() = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestReviewSideEffect>()
    val sideEffect: SharedFlow<QuestReviewSideEffect>
        get() = _sideEffect

    fun setQuestId(questId: Long) {
        _uiState.update {
            it.copy(questId = questId)
        }
    }

    fun onEditClicked(questType: QuestType) {
        viewModelScope.launch {
            _sideEffect.emit(
                if (questType == QuestType.RECORDING) {
                    QuestReviewSideEffect.NavigateToQuestRecording(questId = uiState.value.questId, isEditMode = true)
                } else {
                    QuestReviewSideEffect.NavigateToQuestBehavior(questId = uiState.value.questId, isEditMode = true)
                }
            )
        }
    }

    fun onCancelClicked(){
        viewModelScope.launch {
            _sideEffect.emit(
                QuestReviewSideEffect.NavigateToQuest
            )
        }
    }

    fun getQuestRecordedDetail(questId: Long) {
        viewModelScope.launch {
            val result = questRecordedDetailRepository.getQuestRecordedDetail(questId)
            result.onSuccess { detail ->
                _uiState.update {
                    val imageURL = detail.imageUrl?.takeIf { it.toString() != "null" }?.toString()
                    val newState = it.copy(
                        stepNumber = detail.stepNumber,
                        questNumber = detail.questNumber,
                        createdAt = detail.createdAt,
                        question = detail.question,
                        answer = detail.questAnswer,
                        imageUrl = imageURL,
                        selectedEmotion = LargeTagType.Companion.fromKorean(detail.questEmotionState),
                        emotionDescription = detail.emotionDescription,
                        questType = if (imageURL == null) QuestType.RECORDING else QuestType.ACTIVE
                    )
                    newState
                }
            }.onFailure {
                _sideEffect.emit(
                    QuestReviewSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }
}
