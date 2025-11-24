package com.byeboo.app.presentation.quest.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.getFormattedDate
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.domain.model.quest.QuestRecordingEditModel
import com.byeboo.app.domain.model.quest.QuestRecordingModel
import com.byeboo.app.domain.repository.quest.QuestDetailRecordingRepository
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
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
    val questRecordingRepository: QuestRecordingRepository,
    val questRecordedDetailRepository: QuestRecordedDetailRepository,
    savedStateHandle: SavedStateHandle,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {
    private val questIdArg: Long = checkNotNull(savedStateHandle["questId"])
    private val isEditModeArg: Boolean = savedStateHandle["isEditMode"] ?: false

    private val _uiState = MutableStateFlow(
        QuestRecordingState(
            questId = questIdArg,
            isEditMode = isEditModeArg
        )
    )
    val uiState: StateFlow<QuestRecordingState>
        get() = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestRecordingSideEffect>()
    val sideEffect: SharedFlow<QuestRecordingSideEffect>
        get() = _sideEffect

    init {
        loadQuestInfo()

        if (isEditModeArg) {
            loadRecordedContent()
        }
    }

    private fun loadQuestInfo() {
        viewModelScope.launch {
            val result = questDetailRecordingRepository.getQuestRecordingDetail(questIdArg)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        step = detail.step,
                        stepNumber = detail.stepNumber,
                        questNumber = detail.questNumber,
                        question = detail.question
                    )
                }
            }.onFailure {
                _sideEffect.emit(
                    QuestRecordingSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }

    private fun loadRecordedContent() {
        viewModelScope.launch {
            val result = questRecordedDetailRepository.getQuestRecordedDetail(questIdArg)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        questAnswer = detail.questAnswer
                    )
                }
            }.onFailure {
                QuestRecordingSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
            }
        }
    }

    fun onSaveClicked() {
        val state = uiState.value
        val questId = state.questId
        val questNumber = state.questNumber
        val answer = state.questAnswer
        val emotion = state.selectedEmotion?.title.orEmpty()

        viewModelScope.launch {
            val request = QuestRecordingModel(
                answer = answer,
                questEmotionState = emotion
            )
            val result = questRecordingRepository.postRecording(questId, request)

            result.onSuccess {
                mixpanelUtil.trackEvent(
                    eventName = "quest_success",
                    properties = mapOf(
                        "quest_end_at" to getFormattedDate(),
                        "quest_number" to questNumber,
                        "quest_type" to "질문형",
                        "after_emotion_type" to emotion
                    )
                )
                _uiState.update {
                    it.copy(showBottomSheet = false)
                }
                _sideEffect.emit(
                    QuestRecordingSideEffect.NavigateToQuestRecordingComplete(questId)
                )
            }.onFailure {
                QuestRecordingSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
            }
        }
    }

    private fun onSaveEditClicked() {
        val state = uiState.value
        val questId = state.questId
        val questNumber = state.questNumber
        val answer = state.questAnswer

        viewModelScope.launch {
            val request = QuestRecordingEditModel(answer = answer)
            val result = questRecordingRepository.updateRecording(
                questId = questId,
                request = request
            )

            result.onSuccess{
                mixpanelUtil.trackEvent(
                    eventName = "quest_success",
                    properties = mapOf(
                        "quest_end_at" to getFormattedDate(),
                        "quest_number" to questNumber,
                        "quest_type" to "질문형",
                    )
                )

                _uiState.update {
                    it.copy(isEditMode = false)
                }

                _sideEffect.emit(
                    QuestRecordingSideEffect.NavigateToQuestReview(questId)
                )
            }.onFailure {
                QuestRecordingSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
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
        if (uiState.value.isEditMode) {
            _uiState.update { it.copy(isEditMode = false) }
            viewModelScope.launch {
                _sideEffect.emit(
                    QuestRecordingSideEffect.NavigateUp
                )
            }
        } else {
            _uiState.update { it.copy(showQuitModal = true) }
        }
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

    fun onClickCompleteButton() {
        if (uiState.value.isEditMode) {
            onSaveEditClicked()
        } else {
            openBottomSheet()
        }
    }

    private fun openBottomSheet() {
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
