package com.byeboo.app.presentation.offboarding.offboardingcompleteguide

data class OffboardingCompleteGuideState(
    val nickname: String = "",
    val journeyName: String = ""
)

sealed interface OffboardingCompleteGuideSideEffect {
    data object NavigateToHome : OffboardingCompleteGuideSideEffect
    data object NavigateToOffboardingNewQuest: OffboardingCompleteGuideSideEffect
    data object NavigateToOffboardingCompleteQuest: OffboardingCompleteGuideSideEffect
}