package com.byeboo.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.model.auth.AuthResult
import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SplashStateSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        //checkUserStatusAndNavigate()
    }

    fun startKakaoLogin(isLoginAvailable: Boolean) {
        viewModelScope.launch {
            if (isLoginAvailable) {
                _sideEffect.emit(SplashStateSideEffect.StartKakaoTalkLogin)
            } else {
                _sideEffect.emit(SplashStateSideEffect.StartKakaoWebLogin)
            }
        }
    }

    fun updateLoginResult(token: OAuthToken?, error: Throwable?) {
        viewModelScope.launch {
            when {
                token != null -> {
                    kakaoSignIn(token)
                }

                error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                    //Todo: 스낵바
                }

                error is AuthError -> {
                    _sideEffect.emit(SplashStateSideEffect.StartKakaoWebLogin)
                }

                else -> {
                    // 카카오 로그인 실패
                }
            }
        }
    }

    private fun kakaoSignIn(token: OAuthToken, platform: String = "KAKAO")
    {
        viewModelScope.launch {
                authRepository.loginWithKakao(
                    token = token.accessToken,
                    platform = platform
                ).onSuccess { authResult ->

                }.onFailure { e ->

                }

        }

    }

    // TODO: 소셜 로그인 생기면 로직 변경 예정
    private fun checkUserStatusAndNavigate() {
        viewModelScope.launch {
            delay(1000)

            val isLoggedIn = userRepository.isLoggedIn()

            val effect = if (isLoggedIn) {
                SplashStateSideEffect.NavigateToHome
            } else {
                SplashStateSideEffect.NavigateToTermsOfService
            }

            _sideEffect.emit(effect)
        }
    }
}
