package com.byeboo.app.presentation.splash

sealed interface SplashState {
    data object NavigateToHome : SplashState
    data object NavigateToUserInfo : SplashState
}
