package com.byeboo.app.domain.usecase

import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) {
    private val accessToken: String
        get() = tokenRepository.getCachedAccessToken()

    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logoutAccount(accessToken)
            .onSuccess {
                tokenRepository.clearTokens()
                userRepository.clear()
                tokenRepository.setLoginSplash(true)
            }
    }
}
