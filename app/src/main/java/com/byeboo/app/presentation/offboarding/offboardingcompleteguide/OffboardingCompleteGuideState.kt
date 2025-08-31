package com.byeboo.app.presentation.offboarding.offboardingcompleteguide

data class OffboardingCompleteGuideState(
    val nickname: String = "하츠핑",
    val journeyName: String = "감정 직면"
)

sealed interface OffboardingCompleteGuideSideEffect {
    data object NavigateToHome : OffboardingCompleteGuideSideEffect
    data object NavigateToOffboardingNewJourney: OffboardingCompleteGuideSideEffect
    data object NavigateToOffboardingCompletedJourney: OffboardingCompleteGuideSideEffect
}
