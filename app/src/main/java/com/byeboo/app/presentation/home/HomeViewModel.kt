package com.byeboo.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.model.home.HomeStatus
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    private val questStateRepository: QuestStateRepository,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            userRepository.getNickname().distinctUntilChanged().collect { nickname ->
                _uiState.update {
                    it.copy(nickname = nickname.ifEmpty { "하츠핑" })
                }
            }
        }
        loadInitialData()
    }

    fun refresh() {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val journey = questStateRepository.getUserJourney() ?: "감정 직면"
            val hasSeenAboutHelp = userRepository.hasSeenAboutHelp()

            var status = HomeStatus.INITIAL_START
            var currentStep = 0L
            var hasError = false

            questStateRepository
                .getQuestCount()
                .onSuccess { model ->
                    status = HomeStatus.from(model.userCurrentStatus)
                    currentStep = model.count
                    val journeyStatus = status.toJourneyStatusType()
                    updateJourneyStatus(journeyStatus)
                    mixpanelUtil.trackEvent(
                        eventName = "home_pageview",
                        properties =
                        mapOf(
                            "is_first_pageview" to false,
                            "journey_type" to (questStateRepository.getUserJourney() ?: "추적 실패")
                        )
                    )
                }.onFailure { e ->
                    val errorMessage = e.message.orEmpty()
                    if (!errorMessage.contains("HTTP 404")) {
                        hasError = true
                        _sideEffect.emit(
                            HomeSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                        )
                    }
                }

            _uiState.update {
                it.copy(
                    journey = journey,
                    status = status,
                    currentStep = currentStep,
                    totalSteps = 30,
                    hasSeenAboutHelp = hasSeenAboutHelp,
                    isLoading = false,
                    hasError = hasError
                )
            }
        }
    }

    private fun updateJourneyStatus(newStatus: JourneyStatusType) {
        viewModelScope.launch {
            runCatching {
                val oldStatus = questStateRepository.getUserJourneyStatus().first()
                if (oldStatus != newStatus) {
                    questStateRepository.updateUserJourneyStatus(newStatus)
                }
            }.onFailure { e ->
            }
        }
    }

    private fun HomeStatus.toJourneyStatusType(): JourneyStatusType =
        when (this) {
            HomeStatus.INITIAL_START -> JourneyStatusType.BEFORE_START
            HomeStatus.TODAY_INCOMPLETE, HomeStatus.TODAY_COMPLETE -> JourneyStatusType.IN_PROGRESS
            HomeStatus.JOURNEY_COMPLETE -> JourneyStatusType.COMPLETED
        }

    fun onClickQuest() {
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.NavigateToQuest)
        }
    }

    fun onClickQuestStart() {
        viewModelScope.launch {
            mixpanelUtil.trackEvent(
                eventName = "journey_start_pageview",
                properties =
                mapOf(
                    "journey_type" to _uiState.value.journey
                )
            )
            _sideEffect.emit(HomeSideEffect.NavigateToQuestStart(null))
        }
    }

    fun onHelpIconClicked() {
        viewModelScope.launch {
            mixpanelUtil.trackEvent("tutorial_icon_click")
            mixpanelUtil.trackEvent("tutorial_pageview")
            userRepository.setHasSeenAboutHelp(true)
            _uiState.update { it.copy(hasSeenAboutHelp = true) }
            _sideEffect.emit(HomeSideEffect.NavigateToTutorial)
        }
    }

    fun onOffboardingNewJourneyClicked() {
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.NavigateToOffboardingNewJourney)
        }
    }

    fun onLottieClicked() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showBubble = false
                )
            }

            delay(600)

            _uiState.update {
                it.copy(
                    isBubbleClicked = true,
                    isBubbleEnabled = false,
                    showBubble = true
                )
            }

            delay(3000)

            _uiState.update {
                it.copy(
                    showBubble = false
                )
            }

            delay(600)

            _uiState.update {
                it.copy(
                    isBubbleClicked = false,
                    isBubbleEnabled = true,
                    showBubble = true
                )
            }
        }
    }
}
