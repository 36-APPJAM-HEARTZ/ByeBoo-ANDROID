package com.byeboo.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.model.auth.AuthResult
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.usecase.LoginUseCase
import com.byeboo.app.domain.usecase.ReissueAccessTokenUseCase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val loginUseCase: LoginUseCase,
    private val reissueAccessTokenUseCase: ReissueAccessTokenUseCase
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SplashStateSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    init {
        viewModelScope.launch {
            tokenRepository.initCachedAccessToken()
            startAutoLogin()
        }
    }

    /*private suspend fun startAutoLogin() {
        val cachedToken = tokenRepository.getCachedAccessToken()

        delay(1000)

        if (cachedToken.isNotBlank()) {
            _sideEffect.emit(SplashStateSideEffect.NavigateToHome)
            return
        }

        reissueAccessTokenUseCase()
            .onSuccess {
                _sideEffect.emit(SplashStateSideEffect.NavigateToHome)
            }
            .onFailure {
                _sideEffect.emit(SplashStateSideEffect.ShowLoginButton)
            }

    }

     */

    private suspend fun startAutoLogin() {
        val cachedToken = tokenRepository.getCachedAccessToken()

        if (cachedToken.isNotBlank()) {
            val isRegistered = userRepository.isUserRegistered()

            if (isRegistered) {
                _sideEffect.emit(SplashStateSideEffect.NavigateToHome)
            } else {
                _sideEffect.emit(SplashStateSideEffect.ShowLoginButton)
            }
            return
        }

        reissueAccessTokenUseCase()
            .onSuccess {
                val isRegistered = userRepository.isUserRegistered()
                if (isRegistered) {
                    _sideEffect.emit(SplashStateSideEffect.NavigateToHome)
                } else {
                    _sideEffect.emit(SplashStateSideEffect.ShowLoginButton)
                }
            }
            .onFailure {
                _sideEffect.emit(SplashStateSideEffect.ShowLoginButton)
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
            if (token != null) {
                loginUseCase(token.accessToken, platform = KAKAO)
                    .onSuccess { auth ->
                        if (auth.isRegistered) {
                            _sideEffect.emit(SplashStateSideEffect.NavigateToHome)
                        } else {
                            _sideEffect.emit(SplashStateSideEffect.NavigateToTermsOfService)
                        }
                    }.onFailure {
                        _sideEffect.emit(
                            SplashStateSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                        )
                    }

                when (error) {
                    is ClientError -> {
                        when(error.reason) {
                            ClientErrorCause.Cancelled -> {}
                            else -> {}
                        }
                    }

                    is AuthError -> {
                        _sideEffect.emit(SplashStateSideEffect.StartKakaoWebLogin)
                    }

                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val KAKAO = "KAKAO"
    }
}
