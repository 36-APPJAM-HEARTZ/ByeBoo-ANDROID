package com.byeboo.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.util.LoginType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import com.byeboo.app.domain.usecase.LoginUseCase
import com.byeboo.app.domain.usecase.ReissueAccessTokenUseCase
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val fcmTokenRepository: FcmTokenRepository,
    private val mixpanelUtil: MixpanelUtil,
    private val loginUseCase: LoginUseCase,
    private val reissueAccessTokenUseCase: ReissueAccessTokenUseCase
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SplashStateSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var isRegisteredUser: Boolean = false

    init {
        viewModelScope.launch {
            tokenRepository.initCachedAccessToken()

            if (tokenRepository.restartSplash()) {
                _sideEffect.emit(SplashStateSideEffect.ShowLoginButton)
                return@launch
            }

            startAutoLogin()
        }
    }

    private suspend fun startAutoLogin() {
        val cachedToken = tokenRepository.getCachedAccessToken()

        if (cachedToken.isNotBlank()) {
            if (!mixpanelUtil.hasUserDistinctId()) {
                val userId = userRepository.getUserId()
                userId?.let { id -> mixpanelUtil.setDistinctId(id.toString()) }
            }

            val isRegistered = userRepository.isUserRegistered()
            if (isRegistered) {
                saveFcmToken()
                userRepository.setLoggedIn(true)
                _sideEffect.emit(SplashStateSideEffect.NavigateToHome)
            } else {
                _sideEffect.emit(SplashStateSideEffect.ShowLoginButton)
            }
            return
        }

        reissueAccessTokenUseCase()
            .onSuccess {
                if (!mixpanelUtil.hasUserDistinctId()) {
                    val userId = userRepository.getUserId()
                    userId?.let { id -> mixpanelUtil.setDistinctId(id.toString()) }
                }

                val isRegistered = userRepository.isUserRegistered()
                if (isRegistered) {
                    saveFcmToken()
                    userRepository.setLoggedIn(true)
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
            when {
                token != null -> {
                    loginUseCase(token.accessToken, platform = KAKAO)
                        .onSuccess { auth ->
                            mixpanelUtil.setDistinctId(auth.userId.toString())
                            mixpanelUtil.trackLogin(LoginType.KAKAO, true)
                            userRepository.setLoggedIn(true)

                            isRegisteredUser = auth.isRegistered
                            _sideEffect.emit(SplashStateSideEffect.RequestNotificationPermission)

                        }
                        .onFailure {
                            mixpanelUtil.trackLogin(LoginType.KAKAO, false)
                            _sideEffect.emit(
                                SplashStateSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                            )
                        }
                }

                error != null -> {
                    when (error) {
                        is ClientError -> {
                            if (error.reason != ClientErrorCause.Cancelled) {
                                mixpanelUtil.trackLogin(LoginType.KAKAO, false)
                            }
                        }

                        is AuthError -> {
                            _sideEffect.emit(SplashStateSideEffect.StartKakaoWebLogin)
                        }

                        else -> {
                            mixpanelUtil.trackLogin(LoginType.KAKAO, false)
                        }
                    }
                }
            }
        }
    }

    private fun saveFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            val token = task.result
            viewModelScope.launch {
                withContext(NonCancellable) {
                    fcmTokenRepository.saveFcmToken(FcmTokenModel(token))
                        .onSuccess { Timber.d("Fcm 토큰 성공: $token") }
                        .onFailure { Timber.e(it, "Fcm 토큰 실패") }

                }
            }
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        viewModelScope.launch {
            if (isRegisteredUser) {
                if (isGranted) {
                    fcmTokenRepository.saveAlarmEnabled(true)
                    fcmTokenRepository.allowQuestAlarm()
                }
                _sideEffect.emit(SplashStateSideEffect.NavigateToHome)

            } else {
                if (isGranted) {
                    fcmTokenRepository.saveAlarmEnabled(true)
                }
                _sideEffect.emit(SplashStateSideEffect.NavigateToTermsOfService)
            }
        }
    }

    companion object {
        private const val KAKAO = "KAKAO"
    }
}
