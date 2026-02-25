package com.byeboo.app.presentation.quest.behavior

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.getFormattedDate
import com.byeboo.app.data.mapper.quest.toData
import com.byeboo.app.domain.model.quest.QuestBehaviorEditModel
import com.byeboo.app.domain.model.quest.QuestWritingState
import com.byeboo.app.domain.repository.quest.QuestBehaviorRepository
import com.byeboo.app.domain.repository.quest.QuestDetailBehaviorRepository
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import com.byeboo.app.domain.usecase.UploadImageUseCase
import com.byeboo.app.presentation.quest.behavior.navigation.QuestBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class QuestBehaviorViewModel
    @Inject
    constructor(
        private val questDetailBehaviorRepository: QuestDetailBehaviorRepository,
        private val questRecordedDetailRepository: QuestRecordedDetailRepository,
        private val uploadImageUseCase: UploadImageUseCase,
        private val questBehaviorRepository: QuestBehaviorRepository,
        savedStateHandle: SavedStateHandle,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        private val questIdArg: Long =
            checkNotNull(
                savedStateHandle.toRoute<QuestBehavior.QuestBehaviorWriting>().questId,
            )
        private val isEditModeArg: Boolean = savedStateHandle.toRoute<QuestBehavior.QuestBehaviorWriting>().isEditMode
        private val fromOffboardingArg: Boolean = savedStateHandle.toRoute<QuestBehavior.QuestBehaviorWriting>().fromOffboarding
        private val imageKeyArg: String? = savedStateHandle.toRoute<QuestBehavior.QuestBehaviorWriting>().imageKey

        private val _uiState =
            MutableStateFlow(
                QuestBehaviorState(
                    questId = questIdArg,
                    isEditMode = isEditModeArg,
                    fromOffboarding = fromOffboardingArg,
                    imageKey = imageKeyArg.orEmpty(),
                ),
            )
        val uiState: StateFlow<QuestBehaviorState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestBehaviorSideEffect>()
        val sideEffect: SharedFlow<QuestBehaviorSideEffect> = _sideEffect.asSharedFlow()

        init {
            loadQuestInfo()

            if (isEditModeArg) {
                loadQuestRecordedDetail()
            }
        }

        private fun loadQuestInfo() {
            viewModelScope.launch {
                val result = questDetailBehaviorRepository.getQuestBehaviorDetail(questIdArg)
                result
                    .onSuccess { detail ->
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
                            QuestBehaviorSideEffect.ShowSnackBar(
                                message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                                iconType = CustomSnackBarType.ALERT
                            ),
                        )
                    }
            }
        }

        private fun loadQuestRecordedDetail() {
            viewModelScope.launch {
                val result = questRecordedDetailRepository.getQuestRecordedDetail(questIdArg)
                result
                    .onSuccess { detail ->
                        _uiState.update {
                            it.copy(
                                questAnswer = detail.questAnswer,
                                imageUrl = detail.imageUrl.orEmpty(),
                                imageCount = if (!detail.imageUrl.isNullOrEmpty()) 1 else 0,
                                originalAnswer = detail.questAnswer,
                            )
                        }
                    }.onFailure {
                        _sideEffect.emit(
                            QuestBehaviorSideEffect.ShowSnackBar(
                                message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                                iconType = CustomSnackBarType.ALERT),
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
                val fromOffboarding = state.fromOffboarding

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
                        isEditMode = isEditMode,
                    ).getOrThrow()
                }.onSuccess {
                    if (isEditMode) {
                        mixpanelUtil.trackEvent(
                            eventName = "quest_edit",
                            properties =
                                mapOf(
                                    "quest_end_at" to getFormattedDate(),
                                    "quest_number" to questId,
                                    "quest_type" to "행동형",
                                ),
                        )
                    } else {
                        mixpanelUtil.trackEvent(
                            eventName = "quest_success",
                            properties =
                                mapOf(
                                    "quest_end_at" to getFormattedDate(),
                                    "quest_number" to questId,
                                    "quest_type" to "행동형",
                                    "after_emotion_type" to emotion,
                                ),
                        )
                    }
                    questRecordedDetailRepository.getQuestRecordedDetail(questId)

                    _sideEffect.emit(
                        if (isEditMode) {
                            if (fromOffboarding) {
                                QuestBehaviorSideEffect.NavigateUp
                            } else {
                                QuestBehaviorSideEffect.NavigateToQuestReview(questId)
                            }
                        } else {
                            QuestBehaviorSideEffect.NavigateToQuestBehaviorComplete(questId)
                        },
                    )
                    _sideEffect.emit(QuestBehaviorSideEffect.CompleteAndClear(questId))
                    closeBottomSheet()
                }.onFailure { e ->
                    _sideEffect.emit(
                        QuestBehaviorSideEffect.ShowSnackBar(
                            message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                            iconType = CustomSnackBarType.ALERT),
                    )
                }

                _uiState.update { it.copy(isUploading = false) }
            }
        }

        fun updateSelectedImage(uri: Uri?) {
            _uiState.update { prev ->
                val updated =
                    prev.copy(
                        selectedImageUri = uri,
                        imageCount = if (uri != null) 1 else 0,
                    )

                updated.copy(
                    isCompleteButtonEnabled = completeButtonEnabled(updated),
                )
            }
        }

        fun updateContent(text: String) {
            val contentState =
                if (text.isEmpty()) {
                    QuestWritingState.Ready
                } else {
                    QuestWritingState.Writing
                }

            _uiState.update { prev ->
                val hasAnswerChanged = text != prev.originalAnswer
                val updated =
                    prev.copy(
                        questAnswer = text,
                        contentState = contentState,
                        hasAnswerChanged = hasAnswerChanged,
                    )

                updated.copy(
                    isCompleteButtonEnabled = completeButtonEnabled(updated),
                )
            }
        }

        fun clearQuestInput() {
            _uiState.update {
                it.copy(
                    selectedImageUri = null,
                    imageCount = 0,
                    questAnswer = "",
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
            if (uiState.value.isEditMode || uiState.value.fromOffboarding) {
                viewModelScope.launch {
                    _sideEffect.emit(QuestBehaviorSideEffect.NavigateUp)
                    clearQuestInput()
                }
            } else {
                viewModelScope.launch {
                    _sideEffect.emit(QuestBehaviorSideEffect.NavigateToQuest)
                    delay(NAVIGATION_DELAY_MS)
                    clearQuestInput()
                }
            }
        }

        fun onTipClicked() {
            val questId = uiState.value.questId
            val questNumber = uiState.value.questNumber
            viewModelScope.launch {
                mixpanelUtil.trackEvent(
                    eventName = "quest_tip_pageview",
                    properties =
                        mapOf(
                            "quest_number" to questNumber,
                        ),
                )
                _sideEffect.emit(
                    QuestBehaviorSideEffect.NavigateToQuestTip(questId, QuestType.ACTIVE),
                )
            }
        }

        fun onClickCompleteButton(context: Context) {
            if (uiState.value.isEditMode) {
                if (uiState.value.selectedImageUri == null) {
                    uploadWithoutImageChange()
                } else {
                    uploadImage(context)
                }
            } else {
                openBottomSheet()
            }
        }

        private fun completeButtonEnabled(state: QuestBehaviorState): Boolean {
            val hasImage = state.imageCount > 0

            return if (state.isEditMode) {
                val imageChanged = state.selectedImageUri != null
                state.hasAnswerChanged || imageChanged
            } else {
                hasImage
            }
        }

        private fun uploadWithoutImageChange() {
            viewModelScope.launch {
                val state = uiState.value
                val questId = state.questId
                val imageKey =
                    requireNotNull(state.imageKey) {
                        "It must have imageKey"
                    }
                val result =
                    questBehaviorRepository.updateQuestBehavior(
                        questId = questId,
                        request =
                            QuestBehaviorEditModel(
                                answer = state.questAnswer,
                                imageKey = imageKey,
                            ),
                    )

                result
                    .onSuccess {
                        mixpanelUtil.trackEvent(
                            eventName = "quest_edit",
                            properties =
                                mapOf(
                                    "quest_end_at" to getFormattedDate(),
                                    "quest_number" to questId,
                                    "quest_type" to "행동형",
                                ),
                        )

                        _sideEffect.emit(
                            if (uiState.value.fromOffboarding) {
                                QuestBehaviorSideEffect.NavigateUp
                            } else {
                                QuestBehaviorSideEffect.NavigateToQuestReview(questId)
                            },
                        )
                    }.onFailure {
                        _sideEffect.emit(
                            QuestBehaviorSideEffect.ShowSnackBar(
                                message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                                iconType = CustomSnackBarType.ALERT
                            ),
                        )
                    }
            }
        }

        private fun openBottomSheet() {
            val questNumber = uiState.value.questNumber
            val answer = uiState.value.questAnswer
            mixpanelUtil.trackEvent(
                eventName = "quest_write_success",
                properties =
                    mapOf(
                        "quest_length" to answer.length,
                        "quest_number" to questNumber,
                        "quest_type" to "행동형",
                    ),
            )
            _uiState.update { it.copy(showBottomSheet = true) }
        }

        fun closeBottomSheet() {
            _uiState.update { it.copy(showBottomSheet = false) }
        }

        fun updateSelectedEmotion(emotion: EmotionChipType?) {
            _uiState.update { it.copy(selectedEmotion = emotion) }
        }

        companion object {
            private const val NAVIGATION_DELAY_MS = 300L
        }
    }
