package com.byeboo.app.domain.usecase

import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val fcmTokenRepository: FcmTokenRepository
) {
    private val accessToken: String
        get() = tokenRepository.getCachedAccessToken()

    suspend operator fun invoke(): Result<Unit> {
        val fcmToken = fcmTokenRepository.getFcmToken().orEmpty()
        val logoutResult = authRepository.logoutAccount(accessToken)

        logoutResult.onSuccess {
            fcmTokenRepository.deleteFcmToken(FcmTokenModel(fcmToken))
            tokenRepository.clearTokens()
            userRepository.clear()
            tokenRepository.setLoginSplash(true)
        }
        return logoutResult
    }
}
