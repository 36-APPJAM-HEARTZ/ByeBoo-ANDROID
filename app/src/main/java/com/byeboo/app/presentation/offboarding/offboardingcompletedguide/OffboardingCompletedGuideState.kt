package com.byeboo.app.presentation.offboarding.offboardingcompletedguide

import com.byeboo.app.core.designsystem.type.CustomSnackBarType

data class OffboardingCompletedGuideState(
    val nickname: String = "하츠핑",
    val journeyName: String = "감정 직면",
)

sealed interface OffboardingCompletedGuideSideEffect {
    data object NavigateToHome : OffboardingCompletedGuideSideEffect

    data object NavigateToOffboardingNewJourney : OffboardingCompletedGuideSideEffect

    data object NavigateToOffboardingCompletedJourney : OffboardingCompletedGuideSideEffect

    data class ShowSnackBar(
        val message: String,
        val iconType: CustomSnackBarType,
    ) : OffboardingCompletedGuideSideEffect
}
