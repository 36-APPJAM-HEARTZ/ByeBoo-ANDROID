package com.byeboo.app.presentation.home.homeonboarding

data class HomeOnboardingUiState(
    val showSpeechBubble: Boolean = false,
    val showInstructionText: Boolean = false,
    val isTransitioning: Boolean = false
)

sealed interface HomeOnboardingSideEffect {
    data object NavigateToHome : HomeOnboardingSideEffect
}
