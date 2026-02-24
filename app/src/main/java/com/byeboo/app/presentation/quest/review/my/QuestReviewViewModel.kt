package com.byeboo.app.presentation.quest.review.my

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import com.byeboo.app.presentation.quest.navigation.QuestReview
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
class QuestReviewViewModel
    @Inject
    constructor(
        private val questRecordedDetailRepository: QuestRecordedDetailRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        val questIdArg: Long = savedStateHandle.toRoute<QuestReview>().questId

        private val _uiState =
            MutableStateFlow(
                QuestReviewState(
                    questId = questIdArg,
                ),
            )
        val uiState: StateFlow<QuestReviewState>
            get() = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestReviewSideEffect>()
        val sideEffect: SharedFlow<QuestReviewSideEffect>
            get() = _sideEffect.asSharedFlow()

        init {
            loadQuestRecordedDetail()
        }

        fun onEditClicked(questType: QuestType) {
            viewModelScope.launch {
                _sideEffect.emit(
                    if (questType == QuestType.RECORDING) {
                        QuestReviewSideEffect.NavigateToQuestRecordingEdit(
                            questId = uiState.value.questId,
                            isEditMode = true,
                        )
                    } else {
                        val imageKey =
                            requireNotNull(uiState.value.imageKey) {
                                "Behavior edit must have imageKey"
                            }

                        QuestReviewSideEffect.NavigateToQuestBehaviorEdit(
                            questId = uiState.value.questId,
                            isEditMode = true,
                            imageKey = imageKey,
                        )
                    },
                )
            }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(
                    QuestReviewSideEffect.NavigateToQuest,
                )
            }
        }

        private fun loadQuestRecordedDetail() {
            viewModelScope.launch {
                val result =
                    questRecordedDetailRepository.getQuestRecordedDetail(
                        uiState.value.questId,
                    )
                result
                    .onSuccess { detail ->
                        _uiState.update {
                            val newState =
                                it.copy(
                                    stepNumber = detail.stepNumber,
                                    questNumber = detail.questNumber,
                                    createdAt = detail.createdAt,
                                    question = detail.question,
                                    answer = detail.questAnswer,
                                    imageKey = detail.imageKey.orEmpty(),
                                    imageUrl = detail.imageUrl.orEmpty(),
                                    selectedEmotion =
                                        EmotionChipType.fromKorean(
                                            detail.questEmotionState,
                                        ),
                                    emotionDescription = detail.emotionDescription,
                                    questType = if (detail.imageUrl == null) QuestType.RECORDING else QuestType.ACTIVE,
                                )
                            newState
                        }
                    }.onFailure {
                        _sideEffect.emit(
                            QuestReviewSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요."),
                        )
                    }
            }
        }
    }
