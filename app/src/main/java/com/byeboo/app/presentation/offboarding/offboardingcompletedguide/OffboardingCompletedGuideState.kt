package com.byeboo.app.presentation.offboarding.offboardingcompletedguide

import com.byeboo.app.core.designsystem.type.CustomSnackBarType

data class OffboardingCompletedGuideState(
    val nickname: String = "",
    val journeyName: String = "이별 극복",
)

sealed interface OffboardingCompletedGuideSideEffect {
    data object NavigateToHome : OffboardingCompletedGuideSideEffect

    data object NavigateToQuest : OffboardingCompletedGuideSideEffect

    data object NavigateToOffboardingNewJourney : OffboardingCompletedGuideSideEffect

    data object NavigateToOffboardingCompletedJourney : OffboardingCompletedGuideSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : OffboardingCompletedGuideSideEffect
}
