package com.byeboo.app.presentation.offboarding.offboardingquestreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import com.byeboo.app.presentation.offboarding.navigation.OffboardingQuestReview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffboardingQuestReviewViewModel
    @Inject
    constructor(
        private val questRecordedDetailRepository: QuestRecordedDetailRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val questIdArg = savedStateHandle.toRoute<OffboardingQuestReview>().questId
        private val journeyTypeArg = savedStateHandle.toRoute<OffboardingQuestReview>().journeyType
        private val _uiState =
            MutableStateFlow(
                OffboardingQuestReviewState(
                    questId = questIdArg,
                ),
            )
        val uiState: StateFlow<OffboardingQuestReviewState>
            get() = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<OffboardingQuestReviewSideEffect>()
        val sideEffect: SharedFlow<OffboardingQuestReviewSideEffect>
            get() = _sideEffect.asSharedFlow()

        init {
            loadQuestRecordedDetail()
        }

        fun onEditClicked(questType: QuestType) {
            viewModelScope.launch {
                _sideEffect.emit(
                    if (questType == QuestType.RECORDING) {
                        OffboardingQuestReviewSideEffect.NavigateToQuestRecordingEdit(
                            questId = uiState.value.questId,
                            isEditMode = true,
                            fromOffboarding = true,
                        )
                    } else {
                        val imageKey =
                            requireNotNull(uiState.value.imageKey) {
                                "Behavior edit must have imageKey"
                            }

                        OffboardingQuestReviewSideEffect.NavigateToQuestBehaviorEdit(
                            questId = uiState.value.questId,
                            isEditMode = true,
                            fromOffboarding = true,
                            imageKey = imageKey,
                        )
                    },
                )
            }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(
                    OffboardingQuestReviewSideEffect.NavigateToOffboardingQuestCompleted(
                        journey = journeyTypeArg,
                    ),
                )
            }
        }

        private fun loadQuestRecordedDetail() {
            viewModelScope.launch {
                questRecordedDetailRepository
                    .observeQuestRecordedDetail(questIdArg)
                    .collect { detail ->
                        _uiState.update {
                            it.copy(
                                stepNumber = detail.stepNumber,
                                questNumber = detail.questNumber,
                                createdAt = detail.createdAt,
                                question = detail.question,
                                answer = detail.questAnswer,
                                imageKey = detail.imageKey.orEmpty(),
                                imageUrl = detail.imageUrl.orEmpty(),
                                selectedEmotion = EmotionChipType.fromKorean(detail.questEmotionState),
                                emotionDescription = detail.emotionDescription,
                                questType =
                                    if (detail.imageUrl == null) {
                                        QuestType.RECORDING
                                    } else {
                                        QuestType.ACTIVE
                                    },
                                isExistedAiAnswer = detail.isExistedAiAnswer,
                            )
                        }
                    }
            }
        }

        fun onAiAnswerClicked() {
        }
    }
