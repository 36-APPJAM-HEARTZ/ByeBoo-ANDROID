package com.byeboo.app.presentation.home.homeonboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeOnboardingViewModel @Inject constructor(
    private val questStateRepository: QuestStateRepository,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeOnboardingUiState())
    val uiState: StateFlow<HomeOnboardingUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HomeOnboardingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun startOnboardingAnimation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showSpeechBubble = true)
            delay(2000)
            _uiState.value = _uiState.value.copy(showInstructionText = true)
        }
    }

    fun onHomeLongClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isTransitioning = true)
            mixpanelUtil.trackEvent(
                eventName = "home_pageview",
                properties = mapOf(
                    "is_first_pageview" to true,
                    "journey_type" to (questStateRepository.getUserJourney() ?: "추적 실패")
                )
            )

            mixpanelUtil.trackEvent("onboarding_complete")
            delay(500)
            _sideEffect.emit(HomeOnboardingSideEffect.NavigateToHome)
        }
    }
}