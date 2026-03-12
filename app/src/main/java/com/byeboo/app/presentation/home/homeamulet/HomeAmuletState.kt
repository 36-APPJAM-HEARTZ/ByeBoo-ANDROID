package com.byeboo.app.presentation.home.homeamulet

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.JourneyType

data class HomeAmuletState(
    val journey: JourneyType? = null,
    val journeyDescription: String = "",
    val canFlip: Boolean = false,
)

sealed interface HomeAmuletSideEffect {
    data object NavigateToHomeOnboarding : HomeAmuletSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : HomeAmuletSideEffect
}
