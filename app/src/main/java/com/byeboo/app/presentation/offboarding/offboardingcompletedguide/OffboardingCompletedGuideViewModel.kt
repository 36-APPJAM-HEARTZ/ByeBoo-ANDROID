package com.byeboo.app.presentation.offboarding.offboardingcompletedguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
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
    userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(OffboardingCompletedGuideState())
    val uiState: StateFlow<OffboardingCompletedGuideState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<OffboardingCompletedGuideSideEffect>()
    val sideEffect: SharedFlow<OffboardingCompletedGuideSideEffect> = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            userRepository.getNickname().collect { nickname ->
                _uiState.update { it.copy(nickname = nickname) }
            }
        }
    }

    fun onCloseClicked(){
        viewModelScope.launch {
            _sideEffect.emit(OffboardingCompletedGuideSideEffect.NavigateToHome)
        }
    }

    fun onNewJourneyClicked(){
        viewModelScope.launch {
            _sideEffect.emit(OffboardingCompletedGuideSideEffect.NavigateToOffboardingNewJourney)
        }
    }

    fun onCompletedJourneyClicked(){
        viewModelScope.launch {
            _sideEffect.emit(OffboardingCompletedGuideSideEffect.NavigateToOffboardingCompletedJourney)
        }
    }
}
