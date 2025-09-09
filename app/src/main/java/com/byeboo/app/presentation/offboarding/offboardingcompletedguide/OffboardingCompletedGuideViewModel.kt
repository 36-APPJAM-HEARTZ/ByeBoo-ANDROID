package com.byeboo.app.presentation.offboarding.offboardingcompletedguide

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
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
class OffboardingCompletedGuideViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val questStateRepository: QuestStateRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(OffboardingCompletedGuideState())
    val uiState: StateFlow<OffboardingCompletedGuideState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<OffboardingCompletedGuideSideEffect>()
    val sideEffect: SharedFlow<OffboardingCompletedGuideSideEffect> = _sideEffect.asSharedFlow()

    private val ANIMATION_PLAYED = "animation_played"

    val isInitialAnimation: StateFlow<Boolean> = savedStateHandle.getStateFlow(ANIMATION_PLAYED, false)

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
            val journey = questStateRepository.getUserJourney() ?: "감정 직면"
            _uiState.update { it.copy(journeyName = journey) }
        }
    }

    fun onCloseClicked() {
        viewModelScope.launch { _sideEffect.emit(OffboardingCompletedGuideSideEffect.NavigateToHome) }
    }

    fun onNewJourneyClicked() {
        viewModelScope.launch {
            savedStateHandle[ANIMATION_PLAYED] = true
            _sideEffect.emit(OffboardingCompletedGuideSideEffect.NavigateToOffboardingNewJourney)
        }
    }

    fun onCompletedJourneyClicked() {
        viewModelScope.launch {
            savedStateHandle[ANIMATION_PLAYED] = true
            _sideEffect.emit(OffboardingCompletedGuideSideEffect.NavigateToOffboardingCompletedJourney)
        }
    }
}
