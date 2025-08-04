package com.byeboo.app.presentation.auth.onboarding

import androidx.compose.runtime.Immutable

@Immutable
data class OnboardingState(
    val title: String,
    val imageRes: Int
)

sealed class OnboardingSideEffect {
    object NavigationToUserInfo : OnboardingSideEffect()
}
