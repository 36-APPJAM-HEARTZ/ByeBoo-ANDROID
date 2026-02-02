package com.byeboo.app.domain.usecase

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

class ReissueAccessTokenUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Result<TokenEntity> {
        val refreshToken = tokenRepository.getRefreshToken().firstOrNull().orEmpty()
        if (refreshToken.isBlank()) {
            return Result.failure(IllegalStateException("No RefreshToken"))
        }

        return authRepository
            .reissueAccessToken(refreshToken)
            .onSuccess { tokenRepository.saveTokens(it) }
    }
}
