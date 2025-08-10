package com.byeboo.app.presentation.offboarding.offboardingcompleteguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffboardingCompleteGuideViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(OffboardingCompleteGuideState())
    val uiState: StateFlow<OffboardingCompleteGuideState> = _uiState

    init {
        viewModelScope.launch {
            userRepository.getNickname().collect { nickname ->
                _uiState.update { it.copy(nickname = nickname) }
            }
        }
    }
}