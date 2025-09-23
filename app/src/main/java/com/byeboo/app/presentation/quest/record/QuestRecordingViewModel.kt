package com.byeboo.app.presentation.quest.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.getFormattedDate
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.model.quest.QuestRecording
import com.byeboo.app.domain.repository.quest.QuestDetailRecordingRepository
import com.byeboo.app.domain.repository.quest.QuestRecordingRepository
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
class QuestRecordingViewModel @Inject constructor(
    val questDetailRecordingRepository: QuestDetailRecordingRepository,
    val questRecordingRepository: QuestRecordingRepository,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestRecordingState())
    val uiState: StateFlow<QuestRecordingState>
        get() = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestRecordingSideEffect>()
    val sideEffect: SharedFlow<QuestRecordingSideEffect>
        get() = _sideEffect

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
            }.onFailure {
                _sideEffect.emit(
                    QuestRecordingSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }

    fun postQuestRecording() {
        val questId = uiState.value.questId
        val questNumber = uiState.value.questNumber
        val answer = uiState.value.questAnswer
        val emotion = uiState.value.selectedEmotion?.title.orEmpty()

        viewModelScope.launch {
            val request = QuestRecording(
                answer = answer,
                questEmotionState = emotion
            )
            val result = questRecordingRepository.postRecording(questId, request)

            if (result.isSuccess) {
                mixpanelUtil.trackEvent(
                    eventName = "quest_success",
                    properties = mapOf(
                        "quest_end_at" to getFormattedDate(),
                        "quest_number" to questNumber,
                        "quest_type" to "질문형",
                        "after_emotion_type" to emotion
                    )
                )
                _uiState.update { it.copy(showBottomSheet = false) }
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

    fun onBackClicked() {
        _uiState.update { it.copy(showQuitModal = true) }
    }

    fun onDismissModal() {
        _uiState.update { it.copy(showQuitModal = false) }
    }

    fun onQuitClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestRecordingSideEffect.NavigateToQuest)
        }
    }

    fun onTipClicked() {
        val questId = uiState.value.questId
        val questNumber = uiState.value.questNumber
        viewModelScope.launch {
            mixpanelUtil.trackEvent(
                eventName = "quest_tip_pageview",
                properties = mapOf(
                    "quest_number" to questNumber
                )
            )
            _sideEffect.emit(
                QuestRecordingSideEffect.NavigateToQuestTip(
                    questId,
                    QuestType.RECORDING
                )
            )
        }
    }

    fun openBottomSheet() {
        val questNumber = uiState.value.questNumber
        val answer = uiState.value.questAnswer
        mixpanelUtil.trackEvent(
            eventName = "quest_complete",
            properties = mapOf(
                "quest_length" to answer.length,
                "quest_number" to questNumber,
                "quest_type" to "질문형"
            )
        )
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun closeBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }

    fun updateSelectedEmotion(emotion: LargeTagType?) {
        _uiState.update { it.copy(selectedEmotion = emotion) }
    }
}
