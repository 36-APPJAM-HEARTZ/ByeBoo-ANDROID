package com.byeboo.app.presentation.quest.behavior.complete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.core.util.getFormattedDate
import com.byeboo.app.domain.repository.quest.QuestRecordedDetailRepository
import com.byeboo.app.presentation.quest.behavior.navigation.QuestBehavior
import com.byeboo.app.presentation.quest.navigation.AiAnswerEntryPoint
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
class QuestBehaviorCompleteViewModel
    @Inject
    constructor(
        private val questRecordedDetailRepository: QuestRecordedDetailRepository,
        savedStateHandle: SavedStateHandle,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        private val questIdArg: Long =
            checkNotNull(
                savedStateHandle.toRoute<QuestBehavior.QuestBehaviorComplete>().questId,
            )

        private val _uiState = MutableStateFlow(QuestBehaviorCompleteState(questId = questIdArg))
        val uiState: StateFlow<QuestBehaviorCompleteState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestBehaviorCompleteSideEffect>()
        val sideEffect: SharedFlow<QuestBehaviorCompleteSideEffect> = _sideEffect.asSharedFlow()

        init {
            loadQuestRecordedDetail()
        }

        private fun loadQuestRecordedDetail() {
            viewModelScope.launch {
                questRecordedDetailRepository
                    .getQuestRecordedDetail(questIdArg)
                    .onSuccess { detail ->
                        _uiState.update {
                            it.copy(
                                stepNumber = detail.stepNumber,
                                questNumber = detail.questNumber,
                                createdAt = detail.createdAt,
                                question = detail.question,
                                questAnswer = detail.questAnswer,
                                imageUrl = detail.imageUrl.orEmpty(),
                                selectedEmotion = EmotionChipType.fromKorean(detail.questEmotionState),
                                emotionDescription = detail.emotionDescription,
                                isExistedAiAnswer = detail.isExistedAiAnswer,
                            )
                        }
                    }.onFailure {
                        _sideEffect.emit(
                            QuestBehaviorCompleteSideEffect.ShowSnackBar(
                                snackBarType = CustomSnackBarType.ALERT,
                            ),
                        )
                    }
            }
        }

        fun onCloseClicked() {
            if (uiState.value.questNumber == 30L) {
                viewModelScope.launch {
                    mixpanelUtil.trackEvent(
                        eventName = "journey_complete_pageview",
                        properties =
                            mapOf(
                                "journey_end_at" to getFormattedDate(),
                                "journey_type" to "감정 정리",
                            ),
                    )
                    _sideEffect.emit(
                        QuestBehaviorCompleteSideEffect.NavigateToOffboardingCompletedGuide,
                    )
                }
            } else {
                viewModelScope.launch {
                    _sideEffect.emit(QuestBehaviorCompleteSideEffect.NavigateToQuest)

                    if (uiState.value.questId == 1L) {
                        _sideEffect.emit(QuestBehaviorCompleteSideEffect.ShowInAppReview)
                    }
                }
            }
        }

        fun onAiAnswerClicked() {
            viewModelScope.launch {
                _sideEffect.emit(
                    QuestBehaviorCompleteSideEffect.NavigateToQuestAiAnswer(
                        questId = uiState.value.questId,
                        isExistedAiAnswer = uiState.value.isExistedAiAnswer,
                        aiAnswerEntryPoint = AiAnswerEntryPoint.QUEST,
                    ),
                )
            }
        }
    }
