package com.byeboo.app.presentation.splash

import com.byeboo.app.core.designsystem.type.CustomSnackBarType

sealed interface SplashStateSideEffect {
    data object ShowLoginButton : SplashStateSideEffect

    data object NavigateToHome : SplashStateSideEffect

    data object NavigateToUserInfo : SplashStateSideEffect

    data object NavigateToTermsOfService : SplashStateSideEffect

    data object StartKakaoTalkLogin : SplashStateSideEffect

    data object StartKakaoWebLogin : SplashStateSideEffect

    data object RequestNotificationPermission : SplashStateSideEffect

    data class ShowSnackBar(
        val message: String,
        val iconType: CustomSnackBarType,
    ) : SplashStateSideEffect
}
