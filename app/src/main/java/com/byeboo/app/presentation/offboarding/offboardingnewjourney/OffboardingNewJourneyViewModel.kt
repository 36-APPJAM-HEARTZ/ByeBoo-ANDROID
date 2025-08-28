package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OffboardingNewJourneyViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(OffboardingNewJourneyState())
    val uiState: StateFlow<OffboardingNewJourneyState> = _uiState.asStateFlow()

}
