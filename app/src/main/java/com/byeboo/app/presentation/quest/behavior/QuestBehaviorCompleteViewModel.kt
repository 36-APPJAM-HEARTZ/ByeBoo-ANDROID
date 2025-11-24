package com.byeboo.app.presentation.quest.behavior

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.getFormattedDate
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
class QuestBehaviorCompleteViewModel @Inject constructor(
    val questRecordedDetailRepository: QuestRecordedDetailRepository,
    savedStateHandle: SavedStateHandle,
    private val mixpanelUtil: MixpanelUtil
): ViewModel() {
    private val questIdArg: Long = checkNotNull(savedStateHandle["questId"])

    private val _uiState = MutableStateFlow(QuestBehaviorCompleteState(questId = questIdArg))
    val uiState: StateFlow<QuestBehaviorCompleteState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestBehaviorCompleteSideEffect>()
    val sideEffect: SharedFlow<QuestBehaviorCompleteSideEffect> = _sideEffect

    init {
        loadQuestRecordedDetail()
    }

    private fun loadQuestRecordedDetail() {
        viewModelScope.launch {
            val result = questRecordedDetailRepository.getQuestRecordedDetail(questIdArg)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        stepNumber = detail.stepNumber,
                        questNumber = detail.questNumber,
                        createdAt = detail.createdAt,
                        question = detail.question,
                        questAnswer = detail.questAnswer,
                        imageUrl = detail.imageUrl ?: "",
                        selectedEmotion = LargeTagType.fromKorean(detail.questEmotionState),
                        emotionDescription = detail.emotionDescription
                    )
                }
            }.onFailure {
                _sideEffect.emit(
                    QuestBehaviorCompleteSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }

    fun onCloseClicked() {
        if (uiState.value.questNumber == 30L) {
            viewModelScope.launch {
                mixpanelUtil.trackEvent(
                    eventName = "journey_complete_pageview",
                    properties = mapOf(
                        "journey_end_at" to getFormattedDate(),
                        "journey_type" to "감정 정리"
                    )
                )
                _sideEffect.emit(QuestBehaviorCompleteSideEffect.NavigateToOffboardingCompletedGuide)
            }
        } else {
            viewModelScope.launch {
                _sideEffect.emit(QuestBehaviorCompleteSideEffect.NavigateToQuest)
            }
        }
    }
}
