package com.byeboo.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.usecase.LoginUseCase
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
    private val tokenRepository: TokenRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SplashStateSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    init {
        viewModelScope.launch {
            tokenRepository.initCachedAccessToken()
        }
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
                    val result = loginUseCase(token.accessToken, platform = KAKAO)

                    result.onSuccess { auth ->
                        if (auth.isRegistered) {
                            _sideEffect.emit(SplashStateSideEffect.NavigateToHome)
                        } else {
                            _sideEffect.emit(SplashStateSideEffect.NavigateToTermsOfService)
                        }
                    }.onFailure { e ->
                        //Todo: 스낵바
                    }
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

    companion object {
        private const val KAKAO = "KAKAO"
    }
}
