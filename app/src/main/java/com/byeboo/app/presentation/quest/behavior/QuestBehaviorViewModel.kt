package com.byeboo.app.presentation.quest.behavior

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.domain.model.quest.QuestWritingState
import com.byeboo.app.domain.repository.quest.QuestDetailBehaviorRepository
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import com.byeboo.app.domain.usecase.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuestBehaviorViewModel @Inject constructor(
    private val questDetailBehaviorRepository: QuestDetailBehaviorRepository,
    private val questRecordedDetailRepository: QuestRecordedDetailRepository,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestBehaviorState())
    val uiState: StateFlow<QuestBehaviorState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestBehaviorSideEffect>()
    val sideEffect: SharedFlow<QuestBehaviorSideEffect> = _sideEffect

    fun setQuestId(questId: Long) {
        _uiState.update {
            it.copy(questId = questId)
        }
    }

    fun getQuestDetailInfo(questId: Long) {
        viewModelScope.launch {
            val result = questDetailBehaviorRepository.getQuestBehaviorDetail(questId)
            result.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        stepMissionTitle = detail.step,
                        stepNumber = detail.stepNumber,
                        questNumber = detail.questNumber,
                        question = detail.question
                    )
                }
            }
        }
    }

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
                        imageUrl = detail.imageUrl.toString(),
                        selectedEmotion = LargeTagType.fromKorean(detail.questEmotionState),
                        emotionDescription = detail.emotionDescription
                    )
                }
            }
        }
    }

    fun uploadImage(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }

            val imageUrl = _uiState.value.selectedImageUri ?: return@launch
            val questId = _uiState.value.questId
            val answer = _uiState.value.contents
            val emotion = _uiState.value.selectedEmotion.toData()

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
                    emotion = emotion
                ).getOrThrow()
            }.onSuccess {
                _sideEffect.emit(QuestBehaviorSideEffect.NavigateToQuestBehaviorComplete(questId))
                _sideEffect.emit(QuestBehaviorSideEffect.CompleteAndClear(questId))
                closeBottomSheet()
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
                contents = text,
                contentState = contentState
            )
        }
    }

    fun clearQuestInput() {
        _uiState.update {
            it.copy(
                selectedImageUri = null,
                imageCount = 0,
                contents = ""
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
            _sideEffect.emit(QuestBehaviorSideEffect.NavigateToQuest)
            delay(NAVIGATION_DELAY_MS)
            clearQuestInput()
        }
    }

    fun onTipClicked() {
        val questId = uiState.value.questId
        viewModelScope.launch {
            _sideEffect.emit(QuestBehaviorSideEffect.NavigateToQuestTip(questId, QuestType.ACTIVE))
        }
    }

    fun openBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun closeBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }

    fun isEmotionSelected(isSelected: Boolean) {
        _uiState.update { it.copy(isEmotionSelected = isSelected) }
    }

    fun updateSelectedEmotion(emotion: LargeTagType) {
        _uiState.update { it.copy(selectedEmotion = emotion) }
    }

    fun onCloseClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestBehaviorSideEffect.NavigateToQuest)
        }
    }

    companion object {
        private const val NAVIGATION_DELAY_MS = 300L
    }
}
