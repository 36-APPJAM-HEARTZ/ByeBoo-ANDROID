package com.byeboo.app.presentation.quest.start

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.util.DateUtil.getFormattedDate
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.NewJourneyRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import com.byeboo.app.presentation.quest.navigation.QuestStart
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
class QuestStartViewModel
    @Inject
    constructor(
        private val questStateRepository: QuestStateRepository,
        private val userRepository: UserRepository,
        private val newJourneyRepository: NewJourneyRepository,
        savedStateHandle: SavedStateHandle,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        private val journeyTypeArg = savedStateHandle.toRoute<QuestStart>().journeyType

        private val _uiState = MutableStateFlow(QuestStartState())
        val uiState: StateFlow<QuestStartState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<QuestStartSideEffect>()
        val sideEffect: SharedFlow<QuestStartSideEffect> = _sideEffect.asSharedFlow()

        init {
            loadInitialData()
        }

        private fun loadInitialData() {
            viewModelScope.launch {
                userRepository.getNickname().collect { name ->
                    _uiState.update { it.copy(nickname = name) }
                }
            }
            viewModelScope.launch {
                val journey = questStateRepository.getUserJourney() ?: "재회 준비"
                _uiState.update { it.copy(journeyName = journey) }
            }
            _uiState.update {
                it.copy(journeyType = journeyTypeArg)
            }
        }

        fun onStartClicked() {
            val journey = uiState.value.journeyType

            if (journey == null) {
                viewModelScope.launch {
                    val result = questStateRepository.updateQuestStartState()
                    val journeyType = questStateRepository.getUserJourney() ?: "추적 실패"
                    if (result.isSuccess) {
                        questStateRepository.setQuestStarted(true)
                        questStateRepository.updateUserJourneyStatus(JourneyStatusType.IN_PROGRESS)
                        mixpanelUtil.trackEvent(
                            "journey_start_click",
                            mapOf(
                                "journey_start_at" to getFormattedDate(),
                                "journey_type" to journeyType,
                                "is_first_journey" to true,
                            ),
                        )
                        mixpanelUtil.trackEvent(
                            "quest_pageview",
                            mapOf(
                                "journey_type" to journeyType,
                                "is_first_pageview" to true,
                            ),
                        )
                        _sideEffect.emit(QuestStartSideEffect.NavigateToQuest)
                    } else {
                        _sideEffect.emit(
                            QuestStartSideEffect.ShowSnackBar(
                                snackBarType = CustomSnackBarType.ALERT,
                            ),
                        )
                    }
                }
            } else {
                postNewJourney(journey)
            }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(QuestStartSideEffect.NavigateToHome)
            }
        }

        private fun postNewJourney(journey: JourneyType) {
            val journeyType = journey.journeyType
            val journeyName = journey.journeyName

            viewModelScope.launch {
                newJourneyRepository
                    .postNewJourney(journeyType)
                    .onSuccess {
                        mixpanelUtil.trackEvent(
                            "journey_start_click",
                            mapOf(
                                "journey_start_at" to getFormattedDate(),
                                "journey_type" to journeyName,
                                "is_first_journey" to false,
                            ),
                        )
                        questStateRepository.setQuestStarted(true)
                        questStateRepository.updateUserJourney(journeyName)
                        questStateRepository.updateUserJourneyStatus(JourneyStatusType.IN_PROGRESS)
                        _sideEffect.emit(QuestStartSideEffect.NavigateToQuest)
                    }.onFailure { e ->
                        _sideEffect.emit(
                            QuestStartSideEffect.ShowSnackBar(
                                snackBarType = CustomSnackBarType.ALERT,
                            ),
                        )
                    }
            }
        }
    }
