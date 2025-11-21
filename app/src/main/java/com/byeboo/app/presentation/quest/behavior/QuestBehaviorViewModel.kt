package com.byeboo.app.presentation.quest.behavior

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.getFormattedDate
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.domain.model.quest.QuestWritingState
import com.byeboo.app.domain.repository.quest.QuestDetailBehaviorRepository
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import com.byeboo.app.domain.usecase.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class QuestBehaviorViewModel @Inject constructor(
    private val questDetailBehaviorRepository: QuestDetailBehaviorRepository,
    private val questRecordedDetailRepository: QuestRecordedDetailRepository,
    private val uploadImageUseCase: UploadImageUseCase,
    savedStateHandle: SavedStateHandle,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {
    private val questIdArg: Long = checkNotNull(savedStateHandle["questId"])
    private val isEditModeArg: Boolean = checkNotNull(savedStateHandle["isEditMode"])


    private val _uiState = MutableStateFlow(
        QuestBehaviorState(
            questId = questIdArg,
            isEditMode = isEditModeArg
        )
    )
    val uiState: StateFlow<QuestBehaviorState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestBehaviorSideEffect>()
    val sideEffect: SharedFlow<QuestBehaviorSideEffect> = _sideEffect

    init {
        loadQuestInfo()

        if (isEditModeArg) {
            loadQuestRecordedDetail()
        }
    }

    private fun loadQuestInfo() {
        viewModelScope.launch {
            val result = questDetailBehaviorRepository.getQuestBehaviorDetail(questIdArg)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        step = detail.step,
                        stepNumber = detail.stepNumber,
                        questNumber = detail.questNumber,
                        question = detail.question,
                    )
                }
            }.onFailure {
                _sideEffect.emit(
                    QuestBehaviorSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }

    private fun loadQuestRecordedDetail() {
        viewModelScope.launch {
            val result = questRecordedDetailRepository.getQuestRecordedDetail(questIdArg)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        questAnswer = detail.questAnswer,
                        imageUrl = detail.imageUrl.toString(),
                        imageCount = if (!detail.imageUrl.isNullOrBlank()) 1 else 0
                    )
                }
            }.onFailure {
                _sideEffect.emit(
                    QuestBehaviorSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }
        }
    }

    fun uploadImage(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }

            val state = _uiState.value
            val imageUrl = state.selectedImageUri ?: return@launch
            val questId = state.questId
            val answer = state.questAnswer
            val emotion = state.selectedEmotion?.toData().orEmpty()
            val isEditMode = state.isEditMode

            runCatching {
                val inputStream = context.contentResolver.openInputStream(imageUrl)
                val imageBytes = inputStream?.readBytes() ?: error("이미지 파일을 읽을 수 없습니다.")
                val contentType = context.contentResolver.getType(imageUrl).toString()
                val imageKey = UUID.randomUUID().toString()

                uploadImageUseCase(
                    imageBytes = imageBytes,
                    contentType = contentType,
                    imageKey = imageKey,
                    questId = questId,
                    answer = answer,
                    emotion = emotion,
                    isEditMode = isEditMode
                ).getOrThrow()
            }.onSuccess {
                mixpanelUtil.trackEvent(
                    eventName = "quest_success",
                    properties = mapOf(
                        "quest_end_at" to getFormattedDate(),
                        "quest_number" to questId,
                        "quest_type" to "행동형",
                        "after_emotion_type" to emotion
                    )
                )
                _sideEffect.emit(
                    if (isEditMode) {
                        QuestBehaviorSideEffect.NavigateToQuestReview(questId)
                    } else {
                        QuestBehaviorSideEffect.NavigateToQuestBehaviorComplete(questId)
                    }
                )
                _sideEffect.emit(QuestBehaviorSideEffect.CompleteAndClear(questId))
                closeBottomSheet()
            }.onFailure {
                _sideEffect.emit(
                    QuestBehaviorSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                )
            }

            _uiState.update { it.copy(isUploading = false) }
        }
    }

    fun updateSelectedImage(uri: Uri?) {
        _uiState.update {
            it.copy(
                selectedImageUri = uri,
                imageCount = if (uri != null) 1 else 0
            )
        }
    }

    fun updateContent(text: String) {
        val contentState = if (text.isEmpty()) {
            QuestWritingState.Ready
        } else {
            QuestWritingState.Writing
        }

        _uiState.update {
            it.copy(
                questAnswer = text,
                contentState = contentState
            )
        }
    }

    fun clearQuestInput() {
        _uiState.update {
            it.copy(
                selectedImageUri = null,
                imageCount = 0,
                questAnswer = ""
            )
        }
    }

    fun onBackClicked() {
        if (uiState.value.isEditMode) {
            _uiState.update { it.copy(isEditMode = false) }
            viewModelScope.launch {
                _sideEffect.emit(
                    QuestBehaviorSideEffect.NavigateUp
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
            _sideEffect.emit(QuestBehaviorSideEffect.NavigateToQuest)
            delay(NAVIGATION_DELAY_MS)
            clearQuestInput()
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
            _sideEffect.emit(QuestBehaviorSideEffect.NavigateToQuestTip(questId, QuestType.ACTIVE))
        }
    }

    fun onClickCompleteButton(context: Context) {
        if (uiState.value.isEditMode) {
            if(uiState.value.selectedImageUri == null){
              //  saveEditWithoutImageUpload()
            } else {
                uploadImage(context)
            }
        } else {
            openBottomSheet()
        }
    }

    private fun openBottomSheet() {
        val questNumber = uiState.value.questNumber
        val answer = uiState.value.questAnswer
        mixpanelUtil.trackEvent(
            eventName = "quest_write_success",
            properties = mapOf(
                "quest_length" to answer.length,
                "quest_number" to questNumber,
                "quest_type" to "행동형"
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

    companion object {
        private const val NAVIGATION_DELAY_MS = 300L
    }
}
