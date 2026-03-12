package com.byeboo.app.domain.usecase

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.domain.model.auth.AuthResult
import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import javax.inject.Inject

class LoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val tokenRepository: TokenRepository,
        private val userRepository: UserRepository,
        private val questStateRepository: QuestStateRepository,
    ) {
        suspend operator fun invoke(
            token: String,
            platform: String,
        ): Result<AuthResult> =
            authRepository
                .loginWithKakao(
                    token = token,
                    platform = platform,
                ).mapCatching { auth ->
                    tokenRepository.saveTokens(
                        TokenEntity(
                            accessToken = auth.tokens.accessToken,
                            refreshToken = auth.tokens.refreshToken,
                        ),
                    )

                    auth.name?.let { userRepository.updateUserNickname(it) }
                    questStateRepository.updateUserJourney(auth.journey.journeyName)
                    userRepository.setUserRegistered(auth.isRegistered)
                    userRepository.saveUserId(auth.userId)

                    auth
                }
    }
