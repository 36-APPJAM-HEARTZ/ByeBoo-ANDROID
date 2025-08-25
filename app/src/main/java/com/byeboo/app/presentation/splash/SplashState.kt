package com.byeboo.app.presentation.splash

sealed interface SplashStateSideEffect {
    data object NavigateToHome : SplashStateSideEffect
    data object NavigateToUserInfo : SplashStateSideEffect
    data object NavigateToTermsOfService : SplashStateSideEffect
    data object StartKakaoTalkLogin : SplashStateSideEffect
    data object StartKakaoWebLogin : SplashStateSideEffect
}
