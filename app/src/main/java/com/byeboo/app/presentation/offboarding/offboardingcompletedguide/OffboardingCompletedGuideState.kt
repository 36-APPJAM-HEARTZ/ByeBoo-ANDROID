package com.byeboo.app.presentation.offboarding.offboardingcompletedguide

import com.byeboo.app.core.designsystem.type.CustomSnackBarType

data class OffboardingCompletedGuideState(
    val nickname: String = "하츠핑",
    val journeyName: String = "이별 극복",
)

sealed interface OffboardingCompletedGuideSideEffect {
    data object NavigateToHome : OffboardingCompletedGuideSideEffect

    data object NavigateToOffboardingNewJourney : OffboardingCompletedGuideSideEffect

    data object NavigateToOffboardingCompletedJourney : OffboardingCompletedGuideSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : OffboardingCompletedGuideSideEffect
}
